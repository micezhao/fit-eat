package com.f.a.kidd.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
@ConfigurationProperties(prefix = "customizezuul")
@Data
public class CustomizeZuulProperties  {
	
	private List<String> excludedPaths;
	
}
