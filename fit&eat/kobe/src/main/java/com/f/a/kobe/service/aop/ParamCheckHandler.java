package com.f.a.kobe.service.aop;

/**
 * 参数校验接口选择器
 * @author micezhao
 *
 */
public interface ParamCheckHandler {
	
	boolean commonCheck(Object t);
	
	boolean insertCheck(Object t);
	
	boolean updateCheck(Object t);
	
	boolean binding(Object t);
	
	
}
