package com.f.a.kobe.service.aop;

import java.util.Map;

/**
 * 参数校验接口选择器
 * @author micezhao
 *
 */
public interface ParamCheckHandler {
	
	Map<String, String> commonCheck(Object t,String value);
	
	boolean insertCheck(Object t);
	
	boolean updateCheck(Object t);
	
	boolean binding(Object t);
	
	
}
