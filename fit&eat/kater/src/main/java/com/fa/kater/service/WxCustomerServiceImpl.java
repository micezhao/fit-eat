package com.fa.kater.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fa.kater.customer.pojo.Credential;
import com.fa.kater.customer.pojo.bo.*;
import com.fa.kater.customer.pojo.enums.LoginTypeEnum;
import com.fa.kater.customer.service.impl.CredentialServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class WxCustomerServiceImpl extends CredentialServiceImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    WxRequest wxRequest;

    @Autowired
    RestTemplate restTemplate;

    private static final long KEY_EXPIRE = 1000L*60*60*24;

    @Override
    public boolean existsed(AuthBo authInfoByLoginRequest) {
        Credential customerCredential = new Credential();
        customerCredential.setWxopenid(authInfoByLoginRequest.getOpenid());
        QueryWrapper<Credential> queryWrapper = new QueryWrapper<>(customerCredential);
        //queryWrapper.eq("wxopenid",authInfoByLoginRequest.getOpenid());

        List<Credential> list = this.list(queryWrapper);

        if(list.isEmpty()) {
            return false;
        }
        return true;
    }

    public AuthBo getAuthInfoByLoginRequest(ParamRequest requestAuth) {
        String appId = wxRequest.getAppId();
        String appSecret = wxRequest.getAppSecret();
        String urlPattern = wxRequest.getUrlPattern();


        WxAuthRtn build = new WxAuthRtn().builder().access_token("access_token").expires_in("7000").refresh_token("7789")
                .openid("adasdsa").scope("SCOPE").build();



        String requestURL = MessageFormat.format(urlPattern, appId, appSecret,requestAuth.getCode()).toString();


        String resultwx = "";
        try {
            resultwx = restTemplate.getForObject(requestURL, String.class);
        }catch(Exception e ){
            System.out.println("--------");
            System.out.println(e.getMessage());
            System.out.println("--------");
        }
//        JSONObject forObject = restTemplate.postForObject(requestURL,null, JSONObject.class);

        WxAuthRtn authRtn = JSONObject.parseObject(resultwx,WxAuthRtn.class);

        String code = requestAuth.getCode();
        String result = requestWxAuthInfoByCode1(code);
        WxLoginSuccess wxLoginSuccess = JSONObject.parseObject(result, WxLoginSuccess.class);
        String session_key = wxLoginSuccess.getSession_key();
        String md5Hex = DigestUtils.md5Hex(session_key);
        redisTemplate.opsForValue().set(md5Hex, session_key);
        redisTemplate.expire(md5Hex, KEY_EXPIRE, TimeUnit.SECONDS);
        AuthBo wxAuthResult = new AuthBo();
        //wxAuthResult.setOpenid(wxLoginSuccess.getOpenid());
        wxAuthResult.setOpenid(authRtn.getOpenid());
        wxAuthResult.setAuthToken(md5Hex);
        wxAuthResult.setAuthType(LoginTypeEnum.WECHAT.getLoginTypeCode());
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
