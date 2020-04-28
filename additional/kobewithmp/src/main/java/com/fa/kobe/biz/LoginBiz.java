package com.fa.kobe.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fa.kobe.customer.pojo.Credential;
import com.fa.kobe.customer.pojo.UserInfo;
import com.fa.kobe.customer.pojo.bo.AuthBo;
import com.fa.kobe.customer.pojo.bo.UserAgent;
import com.fa.kobe.customer.pojo.enums.LoginTypeEnum;
import com.fa.kobe.customer.service.CredentialService;
import com.fa.kobe.customer.service.CredentialService;
import com.fa.kobe.customer.service.UserInfoService;
import com.fa.kobe.customer.service.impl.CredentialServiceImpl;
import com.fa.kobe.exceptions.ErrEnum;
import com.fa.kobe.exceptions.InvaildException;
import com.fa.kobe.util.ObjectTransUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginBiz {

    @Autowired
    private Map<String, CredentialServiceImpl> map;


    @Autowired
    private UserInfoService userInfoService;




//    public void createCredential(Credential credential){
//        credentialService.save(credential);
//    }
//
//    public Credential getCredentialById(Long id){
//        return credentialService.getById(id);
//    }

    private static final String PREFFIX = "CustomerServiceImpl";


    public boolean checkExsistedByThirdAuthId(String loginType, String thirdAuthId) {
        boolean exsisted = false;
        CredentialServiceImpl credentialServiceImpl = getServiceInstance(loginType);
        AuthBo authResult = new AuthBo();
        // 3.判断用户是否进入用户授权信息表
        if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
            authResult.setOpenid(thirdAuthId);
            exsisted = credentialServiceImpl.existsed(authResult);
        } else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
            authResult.setOpenid(thirdAuthId);
            exsisted = credentialServiceImpl.existsed(authResult);
        }
        return exsisted;
    }

    public CredentialServiceImpl getServiceInstance(String loginType) {
        if (map.get(loginType + PREFFIX) == null) {
            throw new InvaildException(ErrEnum.NO_INSTANCE.getErrCode(), ErrEnum.NO_INSTANCE.getErrMsg());
        }
        return map.get(loginType + PREFFIX);
    }

    @Transactional
    public UserAgent registerByThirdPart(String thirdAuthId, String loginType) {
        UserAgent userAgent = new UserAgent();
        AuthBo authResult = new AuthBo();
        Credential credential = new Credential();
        CredentialServiceImpl customerCredentialService = getServiceInstance(loginType);
        // 先要生成一条用户信息
        //CustomerBaseInfo customerBaseInfo = new CustomerBaseInfo();
        //customerBaseInfoService.insertCustomerBaseInfo(customerBaseInfo);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserAccount(UUID.randomUUID().toString());
        userInfoService.save(userInfo);

        // 再根据用户信息生成凭证记录
        credential.setUserAccount(userInfo.getUserAccount());


        // 组装userAgent
        userAgent.setUserAccount(userInfo.getUserAccount());
        if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
            userAgent.setWxopenid(thirdAuthId);
            credential.setWxopenid(thirdAuthId);
        } else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
            userAgent.setAliopenid(thirdAuthId);
            credential.setAliopenid(thirdAuthId);
        }
        customerCredentialService.save(credential);

        return userAgent;
    }

    public UserAgent generateUserAgent(String loginType, String thirdAuthId) {

        UserAgent userAgent = new UserAgent();
        userAgent.setLoginType(loginType);
        CredentialServiceImpl customerCredentialService = getServiceInstance(loginType);

        QueryWrapper<Credential> credentialQueryWrapper = new QueryWrapper<>();

        if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
            credentialQueryWrapper.eq("wxopenid",thirdAuthId);
        } else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
            credentialQueryWrapper.eq("aliopenid",thirdAuthId);
        }


        Credential credential = customerCredentialService.getOne(credentialQueryWrapper);
        if(credential == null) {
            throw new InvaildException(ErrEnum.CUSTOMER_NOT_FOUND.getErrCode(), ErrEnum.CUSTOMER_NOT_FOUND.getErrMsg());
        }

        String userAccount = credential.getUserAccount();
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_account",userAccount);
        UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);
        //组装成useragent对象
        ObjectTransUtils.copy(userAgent, credential);
        ObjectTransUtils.copy(userAgent, userInfo);

        return userAgent;
    }


    // 检查这个用户的是否绑定了手机号
    public boolean checkMobileBinded(String mobile, String userAccount,String loginType, String  thirdAuthId) {
        CredentialServiceImpl customerCredentialService = getServiceInstance(loginType);
        //同一手机号不允许在相同渠道注册多次
        QueryWrapper<Credential> credentialQueryWrapper = new QueryWrapper<>();
        credentialQueryWrapper.eq("mobile",mobile);


        List<Credential> listCustomerCredential = customerCredentialService.list(credentialQueryWrapper);

        if(!listCustomerCredential.isEmpty()) {
            Credential customerCredential = listCustomerCredential.get(0);
            if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT && !customerCredential.getWxopenid().isEmpty()) {
                throw new InvaildException(ErrEnum.REDUPICATE_BIND.getErrCode(), ErrEnum.REDUPICATE_BIND.getErrMsg());
            } else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY && !customerCredential.getAliopenid().isEmpty()) {
                throw new InvaildException(ErrEnum.REDUPICATE_BIND.getErrCode(), ErrEnum.REDUPICATE_BIND.getErrMsg());
            }
        }

        // 如果当前的三方授权码不为空，则在检查绑定状态前，先判断当前操作是否存在重复绑定
        if(StringUtils.isNotBlank(thirdAuthId)) {
//            conditional = new CustomerCredential();
            credentialQueryWrapper = new QueryWrapper<>();
            credentialQueryWrapper.eq("user_account",userAccount);
            credentialQueryWrapper.eq("mobile",mobile);

            if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
                credentialQueryWrapper.eq("wxopenid",thirdAuthId);
            } else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
                credentialQueryWrapper.eq("aliopenid",thirdAuthId);
            }
            List<Credential> list= customerCredentialService.list(credentialQueryWrapper);
            //List<CustomerCredential> list= customerCredentialService.listCustomerCredential(conditional);
            if(!list.isEmpty()) {
                throw new InvaildException(ErrEnum.REDUPICATE_BIND.getErrCode(), ErrEnum.REDUPICATE_BIND.getErrMsg());
            }
    }


        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_account",userAccount);
        userInfoQueryWrapper.eq("mobile",mobile);
        List<UserInfo> list = userInfoService.list(userInfoQueryWrapper);
        // List<CustomerBaseInfo> list = customerBaseInfoManager.listByConditional(conditional);
        return list.isEmpty() == true ? false : true;
        //return customerBaseInfoService.hasBinded(customerId, mobile);
    }

    //绑定手机号
    public Credential binding(String mobile, String userAccount, String loginType) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_account",userAccount);
        UserInfo currentUser = userInfoService.getOne(userInfoQueryWrapper);
        currentUser.setMobile(mobile);

        userInfoService.update(currentUser,userInfoQueryWrapper);
        CredentialServiceImpl credentialService = getServiceInstance(loginType);
        QueryWrapper<Credential> credentialQueryWrapper = new QueryWrapper<>();
        credentialQueryWrapper.eq("user_account",userAccount);
        Credential credential = credentialService.getOne(credentialQueryWrapper);
        credential.setMobile(mobile);
        credentialService.update(credential,credentialQueryWrapper);
        return credential;
    }

    //合并用户
    public Credential combine(String mobile, String userAccount, String loginType) {
        CredentialServiceImpl credentialService = getServiceInstance(loginType);
        QueryWrapper<Credential> credentialQueryWrapper = new QueryWrapper<>();
        credentialQueryWrapper.eq("user_account",userAccount);
        Credential source = credentialService.getOne(credentialQueryWrapper);
        credentialQueryWrapper.eq("mobile",mobile);
        Credential destine = credentialService.getOne(credentialQueryWrapper);
        //合并凭证记录
        credentialService.combineCustomerCredential(source, destine, loginType);
        //凭证合并完成后，删除这个新注册的用户信息，绑定后只保留一条用户记录
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_account",userAccount);
        UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);
        userInfoService.removeById(userInfo.getId());
        return destine;
    }
}
