package com.fa.kobe.customer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fa.kobe.customer.pojo.Credential;
import com.fa.kobe.customer.mapper.CredentialMapper;
import com.fa.kobe.customer.pojo.bo.AuthBo;
import com.fa.kobe.customer.pojo.bo.ParamRequest;
import com.fa.kobe.customer.pojo.bo.WxLoginSuccess;
import com.fa.kobe.customer.pojo.enums.LoginTypeEnum;
import com.fa.kobe.customer.service.CredentialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fa.kobe.exceptions.ErrEnum;
import com.fa.kobe.exceptions.InvaildException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jxh
 * @since 2020-04-27
 */
@Service
public class CredentialServiceImpl extends ServiceImpl<CredentialMapper, Credential> implements CredentialService {

    public boolean existsed(AuthBo authResult) {
        return false;
    }

    public void combineCustomerCredential(Credential source, Credential destine,String loginType) {
        //将 source 合并到 destine
        if(LoginTypeEnum.WECHAT.getLoginTypeCode().equalsIgnoreCase(loginType)) {
            destine.setWxopenid(source.getWxopenid());
        }else if(LoginTypeEnum.ALI_PAY.getLoginTypeCode().equalsIgnoreCase(loginType)) {
            destine.setAliopenid(source.getAliopenid());
        }else if(LoginTypeEnum.APP.getLoginTypeCode().equalsIgnoreCase(loginType)){
            destine.setLoginPwd(source.getLoginPwd());
        }else {
            throw new InvaildException(ErrEnum.UNKNOWN_LOGIN_TYPE.getErrCode(),ErrEnum.UNKNOWN_LOGIN_TYPE.getErrMsg());
        }
        this.updateById(destine);
        this.removeById(source);
    }

    public AuthBo getAuthInfoByLoginRequest(ParamRequest requestAuth) {
       return null;
    }

}
