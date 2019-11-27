package com.f.a.kobe.pojo.view;

import java.math.BigDecimal;

import com.f.a.kobe.pojo.CustomerBodyInfo;
import com.f.a.kobe.service.handler.offset.OffsetHandler;
import com.f.a.kobe.service.handler.offset.OffsetStrategy;

public class CustomerBodyInfoView extends CustomerBodyInfo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7123483202920718258L;

	private static final String LINE_UNIT = "cm";

	private static final String PERCENT_UNIT = "%";

	private static final String RATE_UNIT = "次/分";

	private static final String WEIGHT_UNIT = "kg";

	private static final String PRESSURE_UNIT = "mm/Hg";
	
	
	
	private String gender;

	// 腰臀比偏移量
	private String whrOffset;
	
	private String otherWeight;

	// 体脂率偏移量
	private String fatpercentagetOffset;

	// bmi偏移量
	private String bmiOffset;

	// 收缩压偏移量
	private String sdpOffset;

	// 舒张压偏移量
	private String dbpOffset;

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
	public String getOtherWeight() {
		return otherWeight;
	}

	public void setOtherWeight(String otherWeight) {
		this.otherWeight = otherWeight;
	}

	public String getWhrOffset() {
		return whrOffset;
	}

	public void setWhrOffset(String whrOffset) {
		this.whrOffset = whrOffset;
	}

	public String getFatpercentagetOffset() {
		return fatpercentagetOffset;
	}

	public void setFatpercentagetOffset(String fatpercentagetOffset) {
		this.fatpercentagetOffset = fatpercentagetOffset;
	}

	public String getBmiOffset() {
		return bmiOffset;
	}

	public void setBmiOffset(String bmiOffset) {
		this.bmiOffset = bmiOffset;
	}

	public String getSdpOffset() {
		return sdpOffset;
	}

	public void setSdpOffset(String sdpOffset) {
		this.sdpOffset = sdpOffset;
	}

	public String getDbpOffset() {
		return dbpOffset;
	}

	public void setDbpOffset(String dbpOffset) {
		this.dbpOffset = dbpOffset;
	}
	
	public CustomerBodyInfoView build(final CustomerBodyInfo options,final String gender) {
		this.setId(options.getId());
		this.setRecordId(options.getRecordId());
		this.setCustomerId(options.getCustomerId());
		this.gender = gender;
		this.setRegisterDate(options.getRegisterDate());
		this.setHeight(options.getHeight() + LINE_UNIT);
		this.setWeight(options.getWeight() + WEIGHT_UNIT);
		this.setChest(options.getChest() + LINE_UNIT);
		this.setWaistline(options.getWaistline() + LINE_UNIT);
		this.setHipline(options.getHipline() + LINE_UNIT);
		this.setWaistHipRatio(options.getWaistHipRatio());
		this.setLeftArmCircumference(options.getLeftArmCircumference() + LINE_UNIT);
		this.setRightArmCircumference(options.getRightArmCircumference() + LINE_UNIT);
		this.setLeftThighCircumference(options.getLeftThighCircumference() + LINE_UNIT);
		this.setRightThighCircumference(options.getRightThighCircumference() + LINE_UNIT);
		this.setFatPercentage(options.getFatPercentage() + PERCENT_UNIT);
		this.setFatContent(options.getFatContent() + WEIGHT_UNIT);
		BigDecimal otherWeightBd = new BigDecimal(options.getWeight())
							.subtract(new BigDecimal(options.getFatContent()))
							.subtract(new BigDecimal(options.getMuscleContent())).setScale(2,BigDecimal.ROUND_HALF_UP);
		this.setOtherWeight(otherWeightBd.toString()+WEIGHT_UNIT);
		this.setHeartRate(options.getHeartRate() + RATE_UNIT);
		this.setSdp(options.getSdp() + PRESSURE_UNIT);
		this.setDbp(options.getDbp() + PRESSURE_UNIT);
		this.setMuscleContent(options.getMuscleContent() + WEIGHT_UNIT);
		this.setBmi(options.getBmi());
		OffsetHandler handler = new OffsetStrategy().setStrategy(this);
		// 计算偏差值
		this.bmiOffset = handler.bmiOffset(options.getBmi());
		this.fatpercentagetOffset = handler.fatpercentageOffset(options.getFatPercentage());
		this.sdpOffset = handler.sdpOffset(options.getSdp());
		this.dbpOffset = handler.dbpOffset(options.getDbp());
		this.whrOffset = handler.whrOffset(options.getWaistHipRatio());
		return this;
	}

	
	



}
