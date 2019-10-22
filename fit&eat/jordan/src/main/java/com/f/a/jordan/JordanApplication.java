package com.f.a.jordan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class JordanApplication {

	public static void main(String[] args) {
		SpringApplication.run(JordanApplication.class, args);
	}

}
