package com.fa.kater.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切面日志类型
 * @author micezhao
 *
 */
@Documented 
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD) 
public @interface AccessLogAnnot {
	
	public enum AccessLogType{LOG_IN,LOG_OUT,EXPIRE};
	
	AccessLogType logType() default AccessLogType.LOG_IN;
}
