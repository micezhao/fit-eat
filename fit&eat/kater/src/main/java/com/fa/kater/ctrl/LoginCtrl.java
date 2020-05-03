package com.fa.kater.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.f.a.kobe.contants.Contants;
import com.f.a.kobe.view.UserAgent;
import com.fa.kater.biz.LoginBiz;
import com.fa.kater.customer.pojo.Credential;
import com.fa.kater.customer.pojo.bo.AuthBo;
import com.fa.kater.customer.pojo.bo.ParamRequest;
import com.fa.kater.customer.pojo.bo.WxAuthRtn;
import com.fa.kater.customer.pojo.bo.WxRequest;
import com.fa.kater.customer.pojo.enums.LoginTypeEnum;
import com.fa.kater.exceptions.ErrEnum;
import com.fa.kater.exceptions.ErrRtn;
import com.fa.kater.exceptions.InvaildException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@RestController
@RequestMapping("login")
public class LoginCtrl {

    @Autowired
    LoginBiz loginBiz;

    private static final Logger logger = LoggerFactory.getLogger(LoginCtrl.class);

    //这是一种模拟从微信服务端获取授权码的伪实现
    @GetMapping("wxrequest")
    public WxAuthRtn wxrequest(){
        WxAuthRtn build = new WxAuthRtn().builder().access_token("access_token").expires_in("7000").refresh_token("7789")
                .openid("adasdsa").scope("SCOPE").build();
        return build;

    }

    //这是一种模拟从微信服务端获取授权码的伪实现
    @GetMapping("/authCode/{code}/{loginType}")
    public ResponseEntity<AuthBo> getAuthCode(@PathVariable("code") String code,@PathVariable("loginType") String loginType) {
        //2.获取授权结果
    	ParamRequest request=ParamRequest.builder().code(code).loginType(loginType).build(); 
        AuthBo authResult = loginBiz.getServiceInstance(loginType).getAuthInfoByLoginRequest(request);
        logger.info("获取到的第三方code为{}",authResult.getOpenid());
        return new ResponseEntity<AuthBo>(authResult, HttpStatus.OK);
    }

    //注册
    //入参1 code: 第三方的code 如微信openid或者支付宝的openid
    //入参2 loginType: 来自微信或者支付宝
    @PostMapping("/registerByThird")
    public ResponseEntity<Object> registerByThird(@RequestBody ParamRequest request, HttpSession session){
        //获取登录的类型，通过登录的类型可以判断传入的三方id插入到微信或者支付宝的id，并且用哪个凭证的实现类
        String loginType = request.getLoginType();
        String thirdAuthId = request.getCode();

        //检查该类型和三方id是否存在，是否是反复注册
        if (loginBiz.checkExsistedByThirdAuthId(loginType, thirdAuthId)) {
            logger.error(" {}渠道 用户凭证 thirdAuthId ：{} 已经存在", loginType, thirdAuthId);
            return new ResponseEntity<Object>(
                    new ErrRtn(ErrEnum.REDUPICATE_REGISTER.getErrCode(), ErrEnum.REDUPICATE_REGISTER.getErrMsg()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //第一次注册就完成信息填充
        UserAgent userAgent = loginBiz.registerByThirdPart(thirdAuthId, loginType);

        //登录的类型会存在useragent进而存入session，再以后进行其他请求会从useragent里面取得
        session.setAttribute(Contants.USER_AGENT, userAgent); // 将这个用户凭证存入到session中
        return new ResponseEntity<Object>(HttpStatus.OK);
    }


    //登录
    @PostMapping("thirdPart/{loginType}/{thirdAuthId}")
    public ResponseEntity<Object> login(@PathVariable(value="loginType") String loginType,
                                        @PathVariable(value="thirdAuthId") String thirdAuthId,
                                        HttpSession session){
        try {
            //生成UserAgent
            UserAgent userAgent = loginBiz.generateUserAgent(loginType, thirdAuthId);
            //需要对登录信息表进行操作
            loginBiz.recordLoginInfo(userAgent,Contants.LOGIN_TYPE_LOGIN);
            //存入redis
            session.setAttribute(Contants.USER_AGENT, userAgent);
        }catch (InvaildException ex) {
            return new ResponseEntity<Object>(
                    new ErrRtn(ex.getErrCode(), ex.getErrMsg()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(HttpStatus.OK);
    }



    @PostMapping("/binding")
    public ResponseEntity<Object> binding(@RequestBody ParamRequest request, UserAgent userAgent, HttpSession session) {
        String mobile = request.getMobile();
        String userAccount = userAgent.getUserAccount();

        // TODO 检查手机号合法性
        String loginType = userAgent.getLoginType();
        String thirdAuthId = "";
        boolean hasBinded = false;
        if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT
                && StringUtils.isNotBlank(userAgent.getWxopenid())) {
            thirdAuthId = userAgent.getWxopenid();
        } else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY
                && StringUtils.isNotBlank(userAgent.getAliopenid())) {
            thirdAuthId = userAgent.getAliopenid();
        }
        try {
            hasBinded = loginBiz.checkMobileBinded(mobile, userAccount, loginType, thirdAuthId);
        }catch (InvaildException ex) {
            ErrRtn errRtn = new ErrRtn(ex.getErrCode(),ex.getErrMsg());
            logger.info("当前手机号：{} 在已在{}渠道完成绑定，请勿重复绑定", mobile,loginType);
            return new ResponseEntity<Object>(errRtn, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Credential userCredential = new Credential();
        if (!hasBinded) { // 如果这个用户没有绑定过手机号，那就直接执行绑定操作
            logger.info("手机号：[{}] 初次绑定", mobile);
            userCredential = loginBiz.binding(mobile, userAgent.getUserAccount(), loginType);
        } else { // 更新数据
            logger.info("手机号：[{}] 已绑定过，准备进行合并操作", mobile);
            userCredential = loginBiz.combine(mobile, userAgent.getUserAccount(), loginType);
        }
        userAgent.setMobile(userCredential.getMobile());
        if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
            userAgent.setWxopenid(userCredential.getWxopenid());
        } else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
            userAgent.setAliopenid(userCredential.getAliopenid());
        }
        session.setAttribute(Contants.USER_AGENT, userAgent); // 绑定后更新手机号
        return new ResponseEntity<Object>(userAgent, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(UserAgent userAgent, HttpSession session) {
        if (userAgent == null) {
            throw new InvaildException(ErrEnum.UNLOGIN_ERROR.getErrCode(), ErrEnum.UNLOGIN_ERROR.getErrMsg());
        }
        loginBiz.recordLoginInfo(userAgent,Contants.LOGIN_TYPE_LOGOUT);
        boolean logouted = false;
        session.setAttribute(Contants.USER_AGENT, null);
        logouted = true;
        logger.info("用户id：[{}]执行登出操作，已清空会话", userAgent.getUserAccount());
        return new ResponseEntity<Boolean>(logouted, HttpStatus.OK);
    }

}
