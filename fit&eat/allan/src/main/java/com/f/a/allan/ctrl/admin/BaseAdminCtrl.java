package com.f.a.allan.ctrl.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class BaseAdminCtrl {
	
	protected final static String SYMBOL_AND = "-";
	
	/**
	 * adminCtrl 通用方法，将请求条件转成参数集合
	 * @param conditions
	 * @return
	 */
	protected List<String> parse2List(String conditions){
		if(StringUtils.isBlank(conditions)) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		if (StringUtils.contains(conditions, SYMBOL_AND)) {
			String[] statusArray = conditions.split(SYMBOL_AND);
			for (String s : statusArray) {
				if (s.trim().isEmpty()) {
					continue;
				}
				list.add(s);
			}
		} else {
			list.add(conditions);
		}
		return list;
	}
}
