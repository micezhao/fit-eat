package com.f.a.kobe.pojo.response.chart;

import lombok.Data;

@Data
public class LineChart {

	private Long id;

	private Long customerId;

	private String recordId;

	private String registerDate;
	
	/**
	 * 胸围
	 */
	private String chest;
	/**
	 * 腰围
	 */
	private String waistline;
	/**
	 * 臀围
	 */
	private String hipline;
	/**
	 * 腰臀比
	 */
	private String waistHipRatio;
	/**
	 * 左臂围
	 */
	private String leftArmCircumference;
	/**
	 * 右臂围
	 */
	private String rightArmCircumference;
	/**
	 * 左腿围
	 */
	private String leftThighCircumference;
	/**
	 * 右腿围
	 */
	private String rightThighCircumference;

}
