package com.f.a.kobe.service.handler.offset;

import com.f.a.kobe.pojo.enums.OffSetEnum;

public abstract class OffsetHandler   {
	
	private static final int SDP_STANDARD_LEFT = 90;
	
	private static final int SDP_STANDARD_RIGHT = 140;
	
	private static final int DBP_STANDARD_LEFT = 60;
	
	private static final int DBP_STANDARD_RIGHT = 90;
	
	private static final float BMI_STANDARD_LEFT = 18.5f;
	
	private static final float BMI_STANDARD_RIGHT = 25.0f;
	
	private static final float BMI_STANDARD_OVER_RIGHT = 30.0f;
	
	private static final float BMI_STANDARD_OVER_LEVEL_1 = 35.0f;
	
	private static final float BMI_STANDARD_OVER_LEVEL_2 = 40.0f;
	
	
	public String bmiOffset(String bim) {
		Float inputBmi = Float.valueOf(bim);
		if(inputBmi >= BMI_STANDARD_LEFT && inputBmi<= BMI_STANDARD_RIGHT) {
			return OffSetEnum.STANDARD.getCode();
		}
		if(inputBmi >  BMI_STANDARD_RIGHT && inputBmi<= BMI_STANDARD_OVER_RIGHT) {
			return OffSetEnum.FAT_OVER_0.getCode();
		}
		if(inputBmi >  BMI_STANDARD_OVER_RIGHT && inputBmi<= BMI_STANDARD_OVER_LEVEL_1) {
			return OffSetEnum.FAT_OVER_1.getCode();
		}
		if(inputBmi >  BMI_STANDARD_OVER_LEVEL_1 && inputBmi<= BMI_STANDARD_OVER_LEVEL_2) {
			return OffSetEnum.FAT_OVER_2.getCode();
		}
		if(inputBmi >  BMI_STANDARD_OVER_LEVEL_2) {
			return OffSetEnum.FAT_OVER_3.getCode();
		}
		return null;
	}

	public abstract String fatpercentageOffset(String fatpercentaget) ;
	
	
	public String sdpOffset(String sdp) {
		Integer inputSdp = Integer.valueOf(sdp);
		if(SDP_STANDARD_LEFT <= inputSdp  && inputSdp <= SDP_STANDARD_RIGHT) {
			return OffSetEnum.STANDARD.getCode();
		}
		if( inputSdp< SDP_STANDARD_LEFT ) {
			return OffSetEnum.NEGATIVE_MEDIUM.getCode();
		}
		if(inputSdp >= SDP_STANDARD_RIGHT) {
			return OffSetEnum.POSITIVE_MEDIUM.getCode();
		}
		return null;
	}

	public String dbpOffset(String dbp) {
		Integer inputdbp = Integer.valueOf(dbp);
		if(DBP_STANDARD_LEFT <= inputdbp  && inputdbp <= DBP_STANDARD_RIGHT) {
			return OffSetEnum.STANDARD.getCode();
		}
		if( inputdbp< DBP_STANDARD_LEFT ) {
			return OffSetEnum.NEGATIVE_MEDIUM.getCode();
		}
		if(inputdbp >= DBP_STANDARD_RIGHT) {
			return OffSetEnum.POSITIVE_MEDIUM.getCode();
		}
		return null;
	}

	public abstract String whrOffset(String whr);
	
}
