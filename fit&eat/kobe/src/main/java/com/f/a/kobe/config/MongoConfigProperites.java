package com.f.a.kobe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * mongo 数据库连接数据配置类 
 * @author micezhao
 * 通过 @ConfigurationProperties 实现配置文件的配置项加载
 * 通过 @Component 将这个配置项作为一个bean注入到ioc中
 */
@ConfigurationProperties (prefix = "spring.mongo") 
@Data
@Component
public class MongoConfigProperites {
	
	private String host;
	
	private int port;
	
	private String dbName;
	
	private String username;
	
	private String password;
	
	private String url;
}
