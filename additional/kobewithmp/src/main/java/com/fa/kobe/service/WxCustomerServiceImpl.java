package com.fa.kobe.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.fa.kobe.customer.pojo.Credential;
import com.fa.kobe.customer.pojo.bo.AuthBo;
import com.fa.kobe.customer.pojo.bo.ParamRequest;
import com.fa.kobe.customer.pojo.bo.WxLoginSuccess;
import com.fa.kobe.customer.pojo.enums.LoginTypeEnum;
import com.fa.kobe.customer.service.impl.CredentialServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class WxCustomerServiceImpl extends CredentialServiceImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final long KEY_EXPIRE = 1000L*60*60*24;

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

    public AuthBo getAuthInfoByLoginRequest(ParamRequest requestAuth) {
        String code = requestAuth.getCode();
        String result = requestWxAuthInfoByCode1(code);
        WxLoginSuccess wxLoginSuccess = JSONObject.parseObject(result, WxLoginSuccess.class);
        String session_key = wxLoginSuccess.getSession_key();
        String md5Hex = DigestUtils.md5Hex(session_key);
        redisTemplate.opsForValue().set(md5Hex, session_key);
        redisTemplate.expire(md5Hex, KEY_EXPIRE, TimeUnit.SECONDS);
        AuthBo wxAuthResult = new AuthBo();
        wxAuthResult.setOpenid(wxLoginSuccess.getOpenid());
        wxAuthResult.setAuthToken(md5Hex);
        wxAuthResult.setAuthType(LoginTypeEnum.WECHAT.getLoginTypeCode());
        return wxAuthResult;
    }

    private String requestWxAuthInfoByCode1(String code) {

        Random random = new Random();
        int nextInt = random.nextInt();

        String result = "{\"session_key\":\"KdAdSaaNNDAS"+nextInt+"8877JSADN+1==\",\"openid\":\"o"+nextInt+"ndaJdsaJ3omdasmn-LU\"}";
        return result;
    }

}
