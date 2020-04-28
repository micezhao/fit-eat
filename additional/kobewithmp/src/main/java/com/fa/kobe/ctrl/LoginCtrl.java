package com.fa.kobe.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fa.kobe.biz.LoginBiz;
import com.fa.kobe.contants.Contants;
import com.fa.kobe.customer.pojo.Credential;
import com.fa.kobe.customer.pojo.bo.AuthBo;
import com.fa.kobe.customer.pojo.bo.ParamRequest;
import com.fa.kobe.customer.pojo.bo.UserAgent;
import com.fa.kobe.customer.pojo.enums.LoginTypeEnum;
import com.fa.kobe.exceptions.ErrEnum;
import com.fa.kobe.exceptions.ErrRtn;
import com.fa.kobe.exceptions.InvaildException;
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

@RestController
@RequestMapping("login")
public class LoginCtrl {

    @Autowired
    LoginBiz loginBiz;

    private static final Logger logger = LoggerFactory.getLogger(LoginCtrl.class);


    @Resource
    RedisTemplate redisTemplate;

    @PostMapping("redis")
    public String test(@RequestBody AuthBo authbo){
//        String authboStr = JSON.toJSONString(authbo);
//        redisTemplate.opsForValue().set("content1",authboStr);
//        String content = (String)redisTemplate.opsForValue().get("content1");
//        AuthBo authBo = JSONObject.parseObject(content, AuthBo.class);

        redisTemplate.opsForValue().set("content1",authbo);
        return "";
    }


    @GetMapping("/authCode")
    public ResponseEntity<AuthBo> getAuthCode(@RequestBody ParamRequest request) {
        //2.获取授权结果
        AuthBo authResult = loginBiz.getServiceInstance(request.getLoginType()).getAuthInfoByLoginRequest(request);
        return new ResponseEntity<AuthBo>(authResult, HttpStatus.OK);
    }

    //登录
    @PostMapping("thirdPart/{loginType}/{thirdAuthId}")
    public ResponseEntity<Object> login(@PathVariable(value="loginType") String loginType,
                                        @PathVariable(value="thirdAuthId") String thirdAuthId,
                                        HttpSession session){
        try {
            UserAgent userAgent = loginBiz.generateUserAgent(loginType, thirdAuthId);
            session.setAttribute(Contants.USER_AGENT, userAgent);
        }catch (InvaildException ex) {
            return new ResponseEntity<Object>(
                    new ErrRtn(ex.getErrCode(), ex.getErrMsg()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    //注册
    @PostMapping("/register")
    public ResponseEntity<Object> registerByThird(@RequestBody ParamRequest request, HttpSession session){
        String loginType = request.getLoginType();
        String thirdAuthId = "";

        if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
            thirdAuthId = request.getWxopenid();
        }else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
            thirdAuthId = request.getAliopenid();
        }
        boolean exsisted = loginBiz.checkExsistedByThirdAuthId(loginType, thirdAuthId);


        System.out.println(exsisted);
        if (exsisted) {
            logger.error(" {}渠道 用户凭证 thirdAuthId ：{} 已经存在", loginType, thirdAuthId);
            return new ResponseEntity<Object>(
                    new ErrRtn(ErrEnum.REDUPICATE_REGISTER.getErrCode(), ErrEnum.REDUPICATE_REGISTER.getErrMsg()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        UserAgent userAgent = loginBiz.registerByThirdPart(thirdAuthId, loginType);

        userAgent.setLoginType(LoginTypeEnum.getLoginTypeEnum(loginType).getDescription());
        session.setAttribute(Contants.USER_AGENT, userAgent); // 将这个用户凭证存入到session中
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
        boolean logouted = false;
        session.setAttribute(Contants.USER_AGENT, null);
        logouted = true;
        logger.info("用户id：[{}]执行登出操作，已清空会话", userAgent.getUserAccount());
        return new ResponseEntity<Boolean>(logouted, HttpStatus.OK);
    }

}
