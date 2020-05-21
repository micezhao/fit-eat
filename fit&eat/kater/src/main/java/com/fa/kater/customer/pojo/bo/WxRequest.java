package com.fa.kater.customer.pojo.bo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "credential.wechat")
public class WxRequest {

//    @Value("${appId}")
    private String appId;

//    @Value("${credential.wechat.appSecret}")
    private String appSecret;

//    @Value("${credential.wechat.urlPattern}")
    private String urlPattern;

}
