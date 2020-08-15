package com.fa.kater.entity.requset;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginParam {
	
	public String agentId;
	
	public String loginType;
	
	public String authType;
	
	public String thirdAuthId;
	
	public String mobile;
	
	public String smscode;
	
	public String account;
	
	public String password;
	

	
}
