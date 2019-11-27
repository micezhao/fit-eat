package com.f.a.kobe.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	
	
	public static void main(String[] args) {
		DateSelection selection = DateUtils.getDateSelection(6);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(selection.getStratDate()));
		System.out.println(sdf.format(selection.getEndDate()));
	}
}
