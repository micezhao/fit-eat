package com.fa.kater.customer.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PropertySource("application.yml")
public class WxRequest {

    @Value("${credential.wechat.appId}")
    private String appId;

    @Value("${credential.wechat.appSecret}")
    private String appSecret;

    @Value("${credential.wechat.urlPattern}")
    private String urlPattern;

}
