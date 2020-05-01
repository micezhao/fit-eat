package com.fa.kater.util;

import com.fa.kater.exceptions.ErrEnum;
import com.fa.kater.exceptions.InvaildException;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class ObjectTransUtils {
	
	public static void copy(Object dest,Object origin) {
		try {
			BeanUtils.copyProperties(dest, origin);
		} catch (IllegalAccessException | InvocationTargetException e) {
			new InvaildException(ErrEnum.COPY_EXCEPTION.getErrCode(), ErrEnum.COPY_EXCEPTION.getErrMsg());
		}
	}
}
