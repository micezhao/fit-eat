package com.f.a.allan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@ComponentScan("com.f.a.allan")
@MapperScan("com.f.a.allan.dao")
public class AllanApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllanApplication.class, args);
	}

}

