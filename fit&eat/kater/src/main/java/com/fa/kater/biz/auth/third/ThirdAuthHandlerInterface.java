package com.fa.kater.biz.auth.third;

import com.fa.kater.pojo.AgentThirdConfig;

public interface ThirdAuthHandlerInterface {
	
	public String getOpenId(AgentThirdConfig agentConfig,String thirdAuthId);
	
}
