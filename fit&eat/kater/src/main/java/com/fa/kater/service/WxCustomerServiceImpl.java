package com.fa.kater.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.f.a.kobe.contants.Contants;
import com.f.a.kobe.view.UserAgent;
import com.fa.kater.biz.LoginBiz;
import com.fa.kater.customer.pojo.Credential;
import com.fa.kater.customer.pojo.UserInfo;
import com.fa.kater.customer.pojo.bo.AuthBo;
import com.fa.kater.customer.pojo.bo.ParamRequest;
import com.fa.kater.customer.pojo.bo.WxRequest;
import com.fa.kater.customer.service.impl.CredentialServiceImpl;
import com.fa.kater.customer.service.impl.UserInfoServiceImpl;
import com.fa.kater.exceptions.ErrEnum;
import com.fa.kater.exceptions.InvaildException;
import com.fa.kater.util.ObjectTransUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

@Service
public class WxCustomerServiceImpl extends CredentialServiceImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    WxRequest wxRequest;

    @Autowired
    RestTemplate restTemplate;

    private static final long KEY_EXPIRE = 1000L*60*60*24;

    @Autowired
    LoginBiz loginBiz;

    /*@Autowired
    WxCustomerServiceImpl cedentialService;*/

    @Autowired
    UserInfoServiceImpl userInfoService;

    private static final Logger logger = LoggerFactory.getLogger(WxCustomerServiceImpl.class);

    @Override
    public boolean existsed(AuthBo authInfoByLoginRequest) {
        Credential customerCredential = new Credential();
        customerCredential.setWxopenid(authInfoByLoginRequest.getOpenid());
        QueryWrapper<Credential> queryWrapper = new QueryWrapper<>(customerCredential);
        List<Credential> list = this.list(queryWrapper);

        if(list.isEmpty()) {
            return false;
        }
        return true;
    }

    public AuthBo getAuthInfoByLoginRequest(ParamRequest requestAuth, HttpSession httpSession) {

        //获取微信的openid

        //请求参数
        String appId = wxRequest.getAppId();//appId
        String appSecret = wxRequest.getAppSecret();//appSecret
        String urlPattern = wxRequest.getUrlPattern();//url

        //真实请求串
        String requestURL = MessageFormat.format(urlPattern, appId, appSecret,requestAuth.getCode()).toString();


        String resultwx = "";
        String wxopenid = "";
        try {
            resultwx = restTemplate.getForObject(requestURL, String.class);
            JSONObject json = JSONObject.parseObject(resultwx);
            wxopenid = (String) json.get("openid");
        }catch(Exception e ){

            logger.error(e.getMessage());
            throw new InvaildException(ErrEnum.INPUT_PARAM_INVAILD.getErrCode(),ErrEnum.INPUT_PARAM_INVAILD.getErrMsg());
        }

        AuthBo authBo = new AuthBo();
        authBo.setOpenid(wxopenid);
        UserAgent userAgent = new UserAgent();

        //返回 userAgent 还有 是否绑定手机号

        AuthBo wxAuthResult = new AuthBo();

        if(this.existsed(authBo)){
            //如果用户已经存在，返回token，告知是否绑定手机号

            //封装userAgent 根据openid 查 credential 和 userinfo
            Credential credentialConditional = Credential.builder().wxopenid(wxopenid).build();
            QueryWrapper<Credential> credentialQueryWrapper = new QueryWrapper<>(credentialConditional);
            Credential credential = this.getOne(credentialQueryWrapper);


            UserInfo userInfoConditional = UserInfo.builder().userAccount(credential.getUserAccount()).build();
            QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>(userInfoConditional);
            UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);

            ObjectTransUtils.copy(userAgent,userInfo);
            ObjectTransUtils.copy(userAgent,credential);
            userAgent.setUpdateMobile(userInfo.getMobile());



        }else{
            userAgent = loginBiz.registerByThirdPart(wxopenid, requestAuth.getLoginType());
        }


        if(userAgent.getMobile() == null){
            wxAuthResult.setIsBindMobile(1);
        }

        httpSession.setAttribute(Contants.USER_AGENT,userAgent);

        return wxAuthResult;
    }

    private String requestWxAuthInfoByCode1(String code) {

        Random random = new Random();
        int nextInt = random.nextInt();
        int nextInt2 = random.nextInt();
        int nextInt3 = random.nextInt();

        String result = "{\"session_key\":\"KdAdSaaNNDAS"+nextInt+"8877JSADN+1==\",\"openid\":\"o"+nextInt+"ndaJd"+nextInt2+"saJ3o"+nextInt3+"mdasmn-LU\"}";
        return result;
    }

}
