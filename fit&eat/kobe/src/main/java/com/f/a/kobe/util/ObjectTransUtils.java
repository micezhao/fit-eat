package com.f.a.kobe.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;

public class ObjectTransUtils {
	
	public static void copy(Object dest,Object origin) {
		try {
			BeanUtils.copyProperties(dest, origin);
		} catch (IllegalAccessException | InvocationTargetException e) {
			new InvaildException(ErrEnum.COPY_EXCEPTION.getErrCode(), ErrEnum.COPY_EXCEPTION.getErrMsg());
		}
	}
}
