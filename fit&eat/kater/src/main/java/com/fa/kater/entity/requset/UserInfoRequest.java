package com.fa.kater.entity.requset;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {
	
	private String userAccout;
	
	private String agentId;
	
	private String gender;
	
	private String realname;
	
	private String birthday;
	
	private String nickname;
	
	private String headimgUrl;
	
	private String score;
	
	private String mobile;
	
	
}
