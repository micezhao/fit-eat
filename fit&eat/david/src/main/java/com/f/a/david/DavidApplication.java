package com.f.a.david;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class DavidApplication {

	public static void main(String[] args) {
		SpringApplication.run(DavidApplication.class, args);
	}

}
