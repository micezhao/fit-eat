package com.fa.kater.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ParamConfig {

//    @Bean
//    public WxRequest wxRequest(){
//        return new WxRequest();
//    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
