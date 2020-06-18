package com.fa.kater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@ComponentScan({"com.fa","com.f.a"})
public class KaterApplication8764 {
    public static void main(String[] args) {
        SpringApplication.run(KaterApplication8764.class, args);
    }
}
