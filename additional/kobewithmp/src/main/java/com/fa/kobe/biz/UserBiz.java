package com.fa.kobe.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fa.kobe.customer.pojo.UserInfo;
import com.fa.kobe.customer.pojo.bo.UserAgent;
import com.fa.kobe.customer.service.UserInfoService;
import com.fa.kobe.exceptions.ErrEnum;
import com.fa.kobe.exceptions.InvaildException;
import com.fa.kobe.util.ObjectTransUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public class UserBiz {

    @Autowired
    UserInfoService userInfoService;

    private static final Logger logger = LoggerFactory.getLogger(UserBiz.class);


    public UserAgent updateUserInfo(UserInfo userInfo, UserAgent userAgent){

        String userAccount = userAgent.getUserAccount();
        UserInfo userInfoConditional = new UserInfo();
        userInfoConditional.setUserAccount(userAccount);
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>(userInfoConditional);
        UserInfo info = userInfoService.getOne(userInfoQueryWrapper);

        if(info==null){
            logger.error("查询用户信息出现了严重错误！问题来自用户表user_account字段为{}的记录",userAccount);
            throw new InvaildException(ErrEnum.QUERY_PARAM_INVAILD.getErrCode(),ErrEnum.QUERY_PARAM_INVAILD.getErrMsg());
        }



        userInfo.setId(info.getId());
        userInfoService.updateById(userInfo);
        ObjectTransUtils.copy(userAgent,userInfo);
        userAgent.setUpdateMobile(userInfo.getMobile());
        return userAgent;
    }
}
