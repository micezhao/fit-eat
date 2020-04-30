package com.fa.jordan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class JordanApplication8761 {

    public static void main(String[] args) {
        SpringApplication.run(JordanApplication8761.class,args);
    }
}
