package com.f.a.kobe.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Date;

public class QueryParamTransUtil {

	public static <T, R> T formConditionalToCriteria(T criteria, R conditional) {
		// 1 遍历conditional满足条件的属性
		// 找到所有get方法
		Method[] conditionalMethodArrays = conditional.getClass().getMethods();
		Method[] criteriaMethods = criteria.getClass().getMethods();
		for (Method conditionMethod : conditionalMethodArrays) {
			String methodName = conditionMethod.getName();
			if (methodName.contains("get")) {
				try {
					for (Method criteriaMethod : criteriaMethods) {
						String criteriaMethodName = criteriaMethod.getName();
						String criteriaTargetMethodName = MessageFormat.format("and{0}EqualTo", methodName.substring(3));
						if (criteriaMethodName.equalsIgnoreCase(criteriaTargetMethodName)) {
							Object param = conditionMethod.invoke(conditional, null);
							if (param instanceof String) {
								String paramStr = (String) param;
								criteriaMethod.invoke(criteria, paramStr);
							}
							if (param instanceof Date) {
								Date paramDate = (Date) param;
								criteriaMethod.invoke(criteria, paramDate);
							}
							if (param instanceof Long) {
								Long paramLong = (Long) param;
								criteriaMethod.invoke(criteria, paramLong);
							}
							if (param instanceof Integer) {
								int paramInt = (int) param;
								criteriaMethod.invoke(criteria, paramInt);
							}
						}
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return criteria;
	}
}
