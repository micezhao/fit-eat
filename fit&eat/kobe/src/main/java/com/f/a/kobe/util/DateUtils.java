package com.f.a.kobe.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.bo.DateSelection;

public class DateUtils {

	private static final int ONE_MONTH = 1;

	private static final int THREE_MONTH = 3;

	private static final int HALF_YEAR = 6;

	private static final int YEAR = 12;

	public static DateSelection getDateSelection(final int selection) {		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int origin;
		int last;
		Date stratDate = null;
		Date endDate = null;
		last = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, last);
		endDate = calendar.getTime(); // 获取最后一天
		if(selection == ONE_MONTH) {
			calendar.add(Calendar.MONTH, 0);
		}else if (selection == THREE_MONTH) {
			calendar.add(Calendar.MONTH, -2);
		} else if (selection == HALF_YEAR) {
			calendar.add(Calendar.MONTH, -5);
		} else if (selection == YEAR) {
			calendar.add(Calendar.MONTH, -11);
		}
		origin = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, origin);
		stratDate = calendar.getTime();
		return new DateSelection(stratDate, endDate);
	}
	
	public static int sumAge(final String csny) {
		// 根据出生年月求年龄
		// 根据出生年月求年龄 适用于 2018-1-1 类似的日期
		// 解析出生年月
		String[] split = csny.split("-");
		if (split.length != 3) {
			// 给的出生年月不合法
			throw new InvaildException(ErrEnum.INPUT_PARAM_INVAILD.getErrCode(), "出生日期格式不正确");
		}
		String birthMonth = split[1];
		if (split[1].startsWith("0", 0)) {
			birthMonth = split[1].replaceFirst("0", "");
		}
		String birthDay = split[2];
		if (split[1].startsWith("0", 0)) {
			birthDay = split[2].replaceFirst("0", "");
		}
		// 求当前年月日
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		// 算出当前大概年龄
		int age = year - Integer.parseInt(split[0]);
		if (age < 0) {
			throw new InvaildException(ErrEnum.INPUT_PARAM_INVAILD.getErrCode(), "出生年份不能小于当前年份");
		}
		if (month - Integer.parseInt(birthMonth) > 0) {
			// 过生日了
		} else if (month - Integer.parseInt(birthMonth) == 0) {
			// 在本月，不知道有没有过生日，需要用天去判断
			if (day - Integer.parseInt(birthDay) < 0) {
				// 没有过生日
				age -= 1;
			}
		} else {
			// 没有过生日
			age -= 1;
		}
		return age;
	}
	
	public static void main(String[] args) {
		DateSelection selection = DateUtils.getDateSelection(6);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(selection.getStratDate()));
		System.out.println(sdf.format(selection.getEndDate()));
	}
}
