package com.f.a.kobe.pojo.response.chart;

import lombok.Data;

@Data
public class WeightChart {
	
	private Long id;
	
	private Long customerId;
	
	private String recordId;
	
	private String registerDate ;
	
	private String weight;
	
	private String fatContent;
	
	private String muscleContent;
}	
