package com.fa.kater.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fa.kater.customer.pojo.Credential;
import com.fa.kater.customer.pojo.bo.AliLoginSuccess;
import com.fa.kater.customer.pojo.bo.AuthBo;
import com.fa.kater.customer.pojo.bo.ParamRequest;
import com.fa.kater.customer.service.impl.CredentialServiceImpl;
import com.fa.kater.enums.LoginTypeEnum;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AliCustomerServiceImpl extends CredentialServiceImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final long KEY_EXPIRE = 1000L*60*60*24;

    @Override
    public boolean existsed(AuthBo authInfoByLoginRequest) {
        Credential customerCredential = new Credential();
        customerCredential.setAliopenid(authInfoByLoginRequest.getOpenid());
        QueryWrapper<Credential> queryWrapper = new QueryWrapper<>(customerCredential);

        List<Credential> list = this.list(queryWrapper);

        if(list.isEmpty()) {
            return false;
        }
        return true;
    }

    public AuthBo getAuthInfoByLoginRequest(ParamRequest requestAuth, HttpSession httpSession) {
        String code = requestAuth.getCode();
        String result = requestAliAuthInfoByCode1(code);
        AliLoginSuccess aliLoginSuccess = JSONObject.parseObject(result, AliLoginSuccess.class);
        String session_key = aliLoginSuccess.getSession_key();
        String md5Hex = DigestUtils.md5Hex(session_key);
        redisTemplate.opsForValue().set(md5Hex, session_key);
        redisTemplate.expire(md5Hex, KEY_EXPIRE, TimeUnit.SECONDS);
        AuthBo aliAuthResult = new AuthBo();
        aliAuthResult.setOpenid(aliLoginSuccess.getOpenid());
        aliAuthResult.setAuthToken(md5Hex);
        aliAuthResult.setAuthType(LoginTypeEnum.ALI_PAY.getLoginTypeCode());
        return aliAuthResult;
    }

    private String requestAliAuthInfoByCode1(String code) {

        Random random = new Random();
        int nextInt = random.nextInt();
        int nextInt2 = random.nextInt();
        int nextInt3 = random.nextInt();

        String result = "{\"session_key\":\"KdAdSaaNNDAS"+nextInt+"8877JSADN+1==\",\"openid\":\"a"+nextInt+"n9KJld"+nextInt2+"saUIso"+nextInt3+"md9Xf5Sn-LU\"}";
        return result;
    }

}
