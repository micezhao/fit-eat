package com.fa.kobe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
public class KobeApplication8764 {
    public static void main(String[] args) {
        SpringApplication.run(KobeApplication8764.class, args);
    }
}
