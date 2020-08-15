package com.fa.kater.biz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.f.a.kobe.view.UserAgent;
import com.f.a.kobe.view.UserAgent.UserAgentBuilder;
import com.fa.kater.enums.AuthTypeEnum;
import com.fa.kater.enums.LoginTypeEnum;
import com.fa.kater.pojo.ThirdCredential;
import com.fa.kater.pojo.UserInfo;

@Service
public class UserAgentBiz {

	
	/**
	 * 用户代理对象生成器，返回登录用户视图
	 * @param userAccount
	 * @return
	 */
	public  UserAgent generator(String userAccount,String loginType) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserAccount(userAccount);
		userInfo.setDeleted(0);
		userInfo = userInfo.selectOne(new QueryWrapper<UserInfo>(userInfo));
		
		UserAgentBuilder builder = UserAgent.builder();
		builder.hasbinded(false);
		if(StringUtils.isNotBlank(userInfo.getAgentId())) {
			builder.agentId(userInfo.getAgentId());
		}
		if(StringUtils.isNotBlank(userInfo.getUserAccount())) {
			builder.userAccount(userInfo.getUserAccount());
		}
		if(StringUtils.isNotBlank(userInfo.getRealname())) {
			builder.realname(userInfo.getRealname());
		}
		if(StringUtils.isNotBlank(userInfo.getBirthday())) {
			builder.birthday(userInfo.getBirthday());
		}
		if(userInfo.getAge()!=null && userInfo.getAge() != 0) {
			builder.age(userInfo.getAge());
		}
		if(StringUtils.isNotBlank(userInfo.getGender())) {
			builder.gender(userInfo.getGender());
		}
		if(StringUtils.isNotBlank(userInfo.getNickname())) {
			builder.nickname(userInfo.getNickname());
		}
		if(userInfo.getScore()!=null) {
			builder.score(userInfo.getScore());
		}
		if(StringUtils.isNotBlank( userInfo.getMobile())) {
			builder.mobile(userInfo.getMobile());
			builder.hasbinded(true);
		}
		builder.loginType(loginType);
		ThirdCredential credential = new ThirdCredential();
		credential.setUserAccount(userAccount);
		if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			credential.setAuthType(AuthTypeEnum.AUTH_WX.type);
			credential = credential.selectOne(new QueryWrapper<ThirdCredential>(credential));
			builder.wxopenid(credential.getOpenId());
		}else if(LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
			credential.setAuthType(AuthTypeEnum.AUTH_ALI.type);
			builder.aliopenid(credential.getOpenId());
		}else {
			List<ThirdCredential> credentialList = credential.selectList(new QueryWrapper<ThirdCredential>(credential));
			for (ThirdCredential thirdCredential : credentialList) {
				if(thirdCredential.getAuthType() == AuthTypeEnum.AUTH_WX.type) {
					builder.aliopenid(thirdCredential.getOpenId());
				}else if(thirdCredential.getAuthType() == AuthTypeEnum.AUTH_ALI.type) {
					builder.wxopenid(thirdCredential.getOpenId());
				}
			}
		}
		return builder.build();
	}
	
	
}
