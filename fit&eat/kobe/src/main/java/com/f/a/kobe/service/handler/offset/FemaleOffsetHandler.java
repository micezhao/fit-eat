package com.f.a.kobe.service.handler.offset;

import com.f.a.kobe.pojo.enums.OffSetEnum;

/**
 * 女性体征指标偏移计算器
 * @author micezhao
 *
 */
public class FemaleOffsetHandler extends OffsetHandler {	
	

//	private final static int FAT_PERCENTAGE_LEFT = 20;
	
	private final static int FAT_PERCENTAGE_RIGHT = 25;

	@Override
	public String whrOffset(String whr) {
		double d = Double.valueOf(whr);
		if(d< 0.85d) {
			return OffSetEnum.STANDARD.getCode();
		}
		if(d>= 0.85d && d<= 0.89d) {
			return OffSetEnum.NEGATIVE_MIN.getCode();
		}
		if(d>= 0.9d && d<= 0.95d) {
			return OffSetEnum.NEGATIVE_MEDIUM.getCode();
		}
		if(d> 0.95d) {
			return OffSetEnum.NEGATIVE_MAX.getCode();
		}
		return null;
	}

	@Override
	public String fatpercentageOffset(String fatpercentaget) {
		Integer input = Integer.valueOf(fatpercentaget);
		if(input<= FAT_PERCENTAGE_RIGHT) {
			return OffSetEnum.STANDARD.getCode();
		}
		if(input > FAT_PERCENTAGE_RIGHT) {
			return OffSetEnum.FAT_OVER_0.getCode();
		}
		return null;
	}


}
