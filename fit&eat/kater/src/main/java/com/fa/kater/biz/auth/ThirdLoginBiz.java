package com.fa.kater.biz.auth;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.f.a.kobe.view.UserAgent;
import com.f.a.kobe.view.UserAgent.UserAgentBuilder;
import com.fa.kater.annotations.AccessLogAnnot;
import com.fa.kater.annotations.AccessLogAnnot.AccessLogType;
import com.fa.kater.biz.UserInfoBiz;
import com.fa.kater.biz.auth.third.ThirdAuthInterface;
import com.fa.kater.entity.requset.LoginParam;
import com.fa.kater.enums.AuthTypeEnum;
import com.fa.kater.pojo.AgentThirdConfig;
import com.fa.kater.pojo.ThirdCredential;
import com.fa.kater.pojo.UserInfo;
import com.fa.kater.service.impl.ThirdCredentialServiceImpl;
import com.fa.kater.service.impl.UserInfoServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ThirdLoginBiz implements LoginBizInterface{

	@Autowired
	private UserInfoBiz userInfoBiz;
		
	@Autowired
	private ThirdCredentialServiceImpl thirdCredentialServiceImpl;
	
	@Autowired
	private Map<String , ThirdAuthInterface> handlerMap;
	
	@AccessLogAnnot(logType = AccessLogType.LOG_IN )
	@Override
	public UserAgent login(LoginParam param) {
		UserInfo userInfo = existed(param);
		if (userInfo != null) {
			return userInfoBiz.generator(userInfo.getUserAccount(),param.getLoginType(),param.getAuthType());
		}
		UserAgent userAgent = register(param);
		return userAgent;
	}

	public  UserInfo existed(LoginParam param) {
		ThirdAuthInterface thirdAuthHandler = this.getHandlerInstance(param.getAuthType());
		AgentThirdConfig  agentConfig = getAgentThirdConfig(param.getAgentId());
		String openId = thirdAuthHandler.getOpenId(agentConfig, param.getThirdAuthId());
		param.setOpenId(openId);
		QueryWrapper<ThirdCredential> queryWrapper = new QueryWrapper<ThirdCredential>();
		ThirdCredential entity = new ThirdCredential();
		entity.setAgentId(param.getAgentId());
		entity.setAuthType(param.getAuthType());
		entity.setOpenId(openId);
		queryWrapper.setEntity(entity);
		ThirdCredential credential = entity.selectOne(queryWrapper);
		if(credential != null) {
			UserInfo userInfo = new UserInfo();
			userInfo.setUserAccount(credential.getUserAccount());
			userInfo.setDeleted(0);
			QueryWrapper<UserInfo> userInfoqueryWrapper = new QueryWrapper<UserInfo>();
			userInfoqueryWrapper.setEntity(userInfo);
			return userInfo.selectOne(userInfoqueryWrapper);
		}
		return null;
	}
	
	
	/**
	 * @param param
	 * @return
	 */
	@Transactional
	public  UserAgent register(LoginParam param) {
		String agentId = param.getAgentId();
		String authType = param.getAuthType();
		UserInfo userInfo = userInfoBiz.initializeUserInfo(agentId);
		
		//请求第三方接口，并获取第三方的凭证信息
		String openId = param.getOpenId(); // 由于authcode 只能用一次，因此，从请求参数回去
//				getHandlerInstance(authType).getOpenId(getAgentThirdConfig(agentId), param.getThirdAuthId());
		
		ThirdCredential credential = new ThirdCredential();
		credential.setAgentId(agentId);
		credential.setUserAccount(userInfo.getUserAccount());
		credential.setOpenId(openId);
		credential.setAuthType(authType);
		credential.setDeleted(0);
		thirdCredentialServiceImpl.save(credential);
		UserAgentBuilder userAgentBuilder = UserAgent.builder()
							.hasbinded(false).authType(authType)
							.agentId(agentId).loginType(param.getLoginType())
							.userAccount(userInfo.getUserAccount());
		if(AuthTypeEnum.getEnumByType(authType)==AuthTypeEnum.AUTH_WX) {
			userAgentBuilder.wxopenid(openId);
		}else if(AuthTypeEnum.getEnumByType(authType)==AuthTypeEnum.AUTH_ALI) {
			userAgentBuilder.aliopenid(openId);
		}
		return userAgentBuilder.build();
	}
	
	
	@AccessLogAnnot(logType = AccessLogType.LOG_OUT)
	@Override
	public void logout(UserAgent userAgent) {
		// TODO 从缓存中删除当前userAgent
		log.info("用户编号:{}已从系统登出",userAgent.getUserAccount());
	}
	

	/**
	 * 获取第三方配置
	 * @param agentId
	 * @return
	 */
	private  AgentThirdConfig getAgentThirdConfig(String agentId) {
		AgentThirdConfig agentConfig = new AgentThirdConfig();
		agentConfig.setAgentId(agentId);
		agentConfig = agentConfig.selectOne(new QueryWrapper<AgentThirdConfig>(agentConfig));
		return agentConfig; 
	}
	
	/**
	 * 处理类决策器
	 * @param authType
	 * @return
	 */
	private ThirdAuthInterface getHandlerInstance(String authType) {
		if(StringUtils.isBlank(authType)) {
			throw new RuntimeException("authType cannot be null or empty");
		}
		String  handlerName = authType.toLowerCase() + "Handler";
		ThirdAuthInterface handler = handlerMap.get(handlerName);
		if(handler == null) {
			throw new RuntimeException("cannot match a suitable auth-handler ");
		}
		return handler;
	}
	
}
