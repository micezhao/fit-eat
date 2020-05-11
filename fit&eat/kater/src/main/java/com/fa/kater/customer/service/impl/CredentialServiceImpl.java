package com.fa.kater.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fa.kater.customer.mapper.CredentialMapper;
import com.fa.kater.customer.pojo.Credential;
import com.fa.kater.customer.pojo.bo.AuthBo;
import com.fa.kater.customer.pojo.bo.ParamRequest;
import com.fa.kater.customer.pojo.enums.LoginTypeEnum;
import com.fa.kater.customer.service.CredentialService;
import com.fa.kater.exceptions.ErrEnum;
import com.fa.kater.exceptions.InvaildException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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

    public AuthBo getAuthInfoByLoginRequest(ParamRequest requestAuth,  HttpSession httpSession) {
       return null;
    }

}
