package com.f.a.kobe.config.credential;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties (prefix = "credential.wechat") 
@Data
@Component
public class WeChatConfigProperties {

	private String appId;
	
	private String appSecret;
	
	private String urlPattern;
	
	private String errtag;
}
