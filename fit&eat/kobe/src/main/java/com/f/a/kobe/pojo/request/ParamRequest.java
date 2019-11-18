package com.f.a.kobe.pojo.request;

import lombok.Data;

@Data
public class ParamRequest {

	private Long id;

	private Long customerId;

	/** 用户凭证信息参数 --开始-- **/
	private String mobile;

	private String wxOpenid;

	private String aliOpenid;

	private String username;

	private String password;

	private String email;
	/** 用户凭证信息参数 --结束-- **/

	/** 用户地址信息 --开始-- **/
	private Long addrId;

	private String regionId;

	private String regionPid;

	private String useDefault;
	/** 用户地址信息 --结束-- **/

	/** 用户基本信息 --开始-- **/
	private String realname;

	private String birthday;

	private String gender;

	private Integer age;

	private String nickname;

	private String headimg;

	private Integer sorce;
	/** 用户基本信息 --结束-- **/
	
	/** 用户体征信息 --开始-- **/
	private String registerDate;

	private String height;

	private String weight;

	private String chest;

	private String waistline;

	private String hipline;

	private String waistHipRatio;

	private String leftArmCircumference;

	private String rightArmCircumference;

	private String leftThighCircumference;

	private String rightThighCircumference;

	private String fatPercentage;

	private String heartRate;

	private String sdp;

	private String dbp;

	private String fatContent;

	private String muscleContent;
	/** 用户体征 --结束-- **/
}
