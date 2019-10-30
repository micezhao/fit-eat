package com.f.a.kobe.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.CustomerCredentialExample.Criteria;

public class DataUtil {

	public static <T> Criteria formConditionalToCriteria(Criteria criteria, T conditional) {
		// 1 遍历conditional满足条件的属性
		// 找到所有get方法
		Method[] methods = CustomerCredential.class.getMethods();
		Method[] criteriaMethods = criteria.getClass().getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.contains("get")) {
				try {
					for(Method criteriaMethod : criteriaMethods) {
						String name = criteriaMethod.getName();
						if(name.equalsIgnoreCase("and"+methodName.substring(3)+"EqualTo")) {
							Object invoke = method.invoke(conditional, null);
							if(invoke instanceof String) {
								String paramStr = (String)invoke;
								criteriaMethod.invoke(criteria, paramStr);
							}
							if(invoke instanceof Date) {
								Date paramDate = (Date)invoke;
								criteriaMethod.invoke(criteria, paramDate);
							}
							if(invoke instanceof Long) {
								Long paramLong = (Long)invoke;
								criteriaMethod.invoke(criteria, paramLong);
							}
							
						}
						System.out.println(criteriaMethod.getName());
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return criteria;
	}
}
