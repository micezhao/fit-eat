package com.f.a.kobe.service.handler.offset;

import org.apache.commons.lang.StringUtils;

import com.f.a.kobe.pojo.enums.GenderEnum;
import com.f.a.kobe.pojo.view.CustomerBodyInfoView;

public class OffsetStrategy {
	
	public OffsetHandler setStrategy(CustomerBodyInfoView strategy) {
		if(StringUtils.equals(GenderEnum.MALE.getCode(), strategy.getGender()) ) {
			return new MaleOffsetHandler();
		}else {
			return new FemaleOffsetHandler();
		}
	}

}
