package com.f.a.allan.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.kobe.view.UserAgent;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	   @Bean
	    public Docket createRestApi() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .pathMapping("/")
	                .select()
	                .apis(
//	                		RequestHandlerSelectors.basePackage("com.f.a.allan.ctrl") // 根据包名加载
	                		RequestHandlerSelectors.withClassAnnotation(Api.class) // 根据注解加载
	                	) 
	                .build().apiInfo(new ApiInfoBuilder()
	                        .title("allan")
	                        .description("商城及订单系统的接口文档")
	                        .build())
	                .ignoredParameterTypes(UserAgent.class,UserAddress.class);
	    }
}
