package com.f.a.kobe.util;

public class CombinedParamBuilder {
	
	CombinedParam combinedParam;
	
	private Long id;

	private Long customerId;

	private String loginType;

	/** 用户凭证信息参数 --开始-- **/
	private String mobile;

	private String wxOpenid;

	private String aliOpenid;

	private String username;

	private String password;

	private String code;

	private String validateCode;

	private String email;
	/** 用户凭证信息参数 --结束-- **/

	/** 用户地址信息 --开始-- **/
	private Long addrId;

	private String regionId;

	private String regionPid;

	private String provinceNo;

	private String cityNo;

	private String distrcNo;
	
	private String streetNo;

	private String connectorMobile;

	private String connectorName;

	private String useDefault;
	/** 用户地址信息 --结束-- **/

	/** 用户基本信息 --开始-- **/
	private String realname;

	private String birthday;

	private String gender;

	private Integer age;

	private String nickname;

	private String headimg;

	private Integer score;
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
	
	private String addrDetail;
	
	public CombinedParamBuilder() {
		this.combinedParam = new CombinedParam();
	}
	
	public CombinedParamBuilder setStreetNo(String streetNo ) {
		this.combinedParam.setStreetNo(streetNo);
		return this;
	}
	
	public CombinedParamBuilder setAddrDetail(String addrDetail ) {
		this.combinedParam.setAddrDetail(addrDetail);
		return this;
	}
	
	public CombinedParamBuilder setMobile(String mobile ) {
		this.combinedParam.setMobile(mobile);
		return this;
	}
	
	public CombinedParamBuilder setAge(Integer age ) {
		this.combinedParam.setAge(age);
		return this;
	}
	
	public CombinedParamBuilder setCustomerId(Long id ) {
		this.combinedParam.setCustomerId(id);
		return this;
	}
	
	public CombinedParamBuilder setId(Long id) {
		this.combinedParam.setId(id);
		return this;
	}

	public CombinedParamBuilder setLoginType(String loginType) {
		this.combinedParam.setLoginType(loginType);
		return this;
	}

	public CombinedParamBuilder setWxOpenid(String wxOpenid) {
		this.combinedParam.setWxOpenid(wxOpenid);
		return this;
	}

	public CombinedParamBuilder setAliOpenid(String aliOpenid) {
		this.combinedParam.setAliOpenid(aliOpenid);
		return this;
	}

	public CombinedParamBuilder setUsername(String username) {
		this.combinedParam.setUsername(username);
		return this;
	}

	public CombinedParamBuilder setPassword(String password) {
		this.combinedParam.setPassword(password);
		return this;
	}

	public CombinedParamBuilder setCode(String code) {
		this.combinedParam.setCode(code);
		return this;
	}

	public CombinedParamBuilder setValidateCode(String validateCode) {
		this.combinedParam.setValidateCode(validateCode);
		return this;
	}

	public CombinedParamBuilder setEmail(String email) {
		this.combinedParam.setEmail(email);
		return this;
	}

	public CombinedParamBuilder setAddrId(Long addrId) {
		this.combinedParam.setAddrId(addrId);
		return this;
	}

	public CombinedParamBuilder setRegionId(String regionId) {
		this.combinedParam.setRegionId(regionId);
		return this;
	}

	public CombinedParamBuilder setRegionPid(String regionPid) {
		this.combinedParam.setRegionPid(regionPid);
		return this;
	}

	public CombinedParamBuilder setProvinceNo(String provinceNo) {
		this.combinedParam.setProvinceNo(provinceNo);
		return this;
	}

	public CombinedParamBuilder setCityNo(String cityNo) {
		this.combinedParam.setCityNo(cityNo);
		return this;
	}

	public CombinedParamBuilder setDistrcNo(String districtNo) {
		this.combinedParam.setDistrcNo(districtNo);
		return this;
	}

	public CombinedParamBuilder setConnectorMobile(String connectorMobile) {
		this.combinedParam.setConnectorMobile(connectorMobile);
		return this;
	}

	public CombinedParamBuilder setConnectorName(String connectorName) {
		this.combinedParam.setConnectorName(connectorName);
		return this;
	}

	public CombinedParamBuilder setUseDefault(String useDefault) {
		this.combinedParam.setUseDefault(useDefault);
		return this;
	}

	public CombinedParamBuilder setRealname(String realname) {
		this.combinedParam.setRealname(realname);
		return this;
	}

	public CombinedParamBuilder setBirthday(String birthday) {
		this.combinedParam.setBirthday(birthday);
		return this;
	}

	public CombinedParamBuilder setGender(String gender) {
		this.combinedParam.setGender(gender);
		return this;
	}

	public CombinedParamBuilder setNickname(String nickname) {
		this.combinedParam.setNickname(nickname);
		return this;
	}

	public CombinedParamBuilder setHeadimg(String headimg) {
		this.combinedParam.setHeadimg(headimg);
		return this;
	}

	public CombinedParamBuilder setSorce(Integer score) {
		this.combinedParam.setScore(score);
		return this;
	}

	public CombinedParamBuilder setRegisterDate(String registerDate) {
		this.combinedParam.setRegisterDate(registerDate);
		return this;
	}

	public CombinedParamBuilder setHeight(String height) {
		this.combinedParam.setHeight(height);
		return this;
	}

	public CombinedParamBuilder setWeight(String weight) {
		this.combinedParam.setWeight(weight);
		return this;
	}

	public CombinedParamBuilder setChest(String chest) {
		this.combinedParam.setChest(chest);
		return this;
	}

	public CombinedParamBuilder setWaistline(String waistline) {
		this.combinedParam.setWaistline(waistline);
		return this;
	}

	public CombinedParamBuilder setHipline(String hipline) {
		this.combinedParam.setHipline(hipline);
		return this;
	}

	public CombinedParamBuilder setWaistHipRatio(String waistHipRatio) {
		this.combinedParam.setWaistHipRatio(waistHipRatio);
		return this;
	}

	public CombinedParamBuilder setLeftArmCircumference(String leftArmCircumference) {
		this.combinedParam.setLeftArmCircumference(leftArmCircumference);
		return this;
	}

	public CombinedParamBuilder setRightArmCircumference(String rightArmCircumference) {
		this.combinedParam.setRightArmCircumference(rightArmCircumference);
		return this;
	}

	public CombinedParamBuilder setLeftThighCircumference(String leftThighCircumference) {
		this.combinedParam.setLeftThighCircumference(leftThighCircumference);
		return this;
	}

	public CombinedParamBuilder setRightThighCircumference(String rightThighCircumference) {
		this.combinedParam.setRightThighCircumference(rightThighCircumference);
		return this;
	}

	public CombinedParamBuilder setFatPercentage(String fatPercentage) {
		this.combinedParam.setFatPercentage(fatPercentage);
		return this;
	}

	public CombinedParamBuilder setHeartRate(String heartRate) {
		this.combinedParam.setHeartRate(heartRate);
		return this;
	}

	public CombinedParamBuilder setSdp(String sdp) {
		this.combinedParam.setSdp(sdp);
		return this;
	}

	public CombinedParamBuilder setDbp(String dbp) {
		this.combinedParam.setDbp(dbp);
		return this;
	}

	public CombinedParamBuilder setFatContent(String fatContent) {
		this.combinedParam.setFatContent(fatContent);
		return this;
	}

	public CombinedParamBuilder setMuscleContent(String muscleContent) {
		this.combinedParam.setMuscleContent(muscleContent);
		return this;
	}

	public CombinedParam build() {
		return this.combinedParam;
	}
}
