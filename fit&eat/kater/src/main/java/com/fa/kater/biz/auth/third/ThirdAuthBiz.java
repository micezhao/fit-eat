package com.fa.kater.biz.auth.third;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.f.a.kobe.view.UserAgent;
import com.fa.kater.biz.auth.LoginBiz;
import com.fa.kater.entity.requset.LoginParam;
import com.fa.kater.pojo.AgentThirdConfig;
import com.fa.kater.pojo.ThirdCredential;
import com.fa.kater.pojo.UserInfo;
import com.fa.kater.service.impl.ThirdCredentialServiceImpl;
import com.fa.kater.service.impl.UserInfoServiceImpl;

/**
 * 用于处理第三方登录类
 * @author micezhao
 *
 */
@Service
public class ThirdAuthBiz extends LoginBiz{
	
	@Autowired
	private UserInfoServiceImpl userInfoServiceImpl;
	
	@Autowired
	private ThirdCredentialServiceImpl thirdCredentialServiceImpl;
	
	@Autowired
	private Map<String , ThirdAuthHandlerInterface> handlerMap;
	
	
	
	@Override
	public UserAgent register(LoginParam param) {
		String agentId = param.getAgentId();
		String authType = param.getAuthType();
		UserInfo userInfo = new UserInfo();
		userInfo.setAgentId(agentId);
		userInfo.setUserAccount(UUID.randomUUID().toString());
		userInfoServiceImpl.save(userInfo); // 生成一个编号
		//TODO 请求第三方接口，并获取第三方的凭证信息
		String openId = "";
		ThirdCredential credential = new ThirdCredential();
		credential.setAgentId(agentId);
		credential.setUserAccount(userInfo.getUserAccount());
		credential.setOpenId(openId);
		credential.setAuthType(authType);
		credential.setDeleted(0);
		thirdCredentialServiceImpl.save(credential);
		
		return null;
	}

	@Override
	public UserInfo existed(LoginParam param) {
		
		ThirdAuthHandlerInterface thirdAuthHandler = this.getHandlerInstance(param.getAuthType());
		AgentThirdConfig  agentConfig = getAgentThirdConfig(param.getAgentId());
		String openId = thirdAuthHandler.getOpenId(agentConfig, param.getThirdAuthId());
		
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
	 * 获取第三方配置
	 * @param agentId
	 * @return
	 */
	private AgentThirdConfig getAgentThirdConfig(String agentId) {
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
	private ThirdAuthHandlerInterface getHandlerInstance(String authType) {
		String handlerName = authType+"handler";
		return handlerMap.get(handlerName);
	}
	
}
