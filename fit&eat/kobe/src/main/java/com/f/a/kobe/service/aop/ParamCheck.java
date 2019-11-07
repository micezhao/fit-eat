package com.f.a.kobe.service.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD}) //基于Spring AOP的注解只能作用在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamCheck {

	String value();

}
