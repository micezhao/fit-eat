package com.f.a.allan.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.mongodb.core.query.Update;

import com.f.a.allan.exceptions.ErrEnum;
import com.f.a.allan.exceptions.InvaildException;

public class ObjectUtils {

	public static <T> String[] getBlankFields(T t) {

		Field[] fields = t.getClass().getDeclaredFields();
		List<String> blankFieldList = new ArrayList<String>();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if (field.get(t) == null || "".equals(field.get(t))) {
					blankFieldList.add(field.getName());
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!blankFieldList.isEmpty()) {
			return blankFieldList.toArray(new String[blankFieldList.size()]);
		}
		return null;
	}

	public static <T> Update getUpdateFields(T t) {

		Field[] fields = t.getClass().getDeclaredFields();
//		List<String>  blankFieldList = new ArrayList<String>();
		Update update = new Update();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				System.out.println(field.get(t));
				if (field.get(t) != null) {
					update.set(field.getName(), field.get(t));
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return update;
	}

	public static void copy(Object dest, Object origin) {
		try {
			BeanUtils.copyProperties(dest, origin);
		} catch (IllegalAccessException | InvocationTargetException e) {
			new InvaildException(ErrEnum.COPY_EXCEPTION.getErrCode(), ErrEnum.COPY_EXCEPTION.getErrMsg());
		}
	}

}
