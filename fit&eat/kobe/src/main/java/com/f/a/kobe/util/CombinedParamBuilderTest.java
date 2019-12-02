package com.f.a.kobe.util;

import java.util.Arrays;
import java.util.List;

//封装需要校验的类型
public class CombinedParamBuilderTest {
	
	CombinedParamCheckor combinedParamCheckor;
	
	public CombinedParamBuilderTest setMobileArrays(String ... arrays) {
		List<String> list = Arrays.asList(arrays);
		this.combinedParamCheckor.setMobileList(list);
		return this;
	}
	
	public CombinedParamBuilderTest setFloatArrays(String ... arrays) {
		List<String> list = Arrays.asList(arrays);
		this.combinedParamCheckor.setFloatList(list);
		return this;
	}
	
	//名字
	public CombinedParamBuilderTest setRealName(String realName ) {
		this.combinedParamCheckor.setRealName(realName);
		return this;
	}
	
	//冗余名字1
	public CombinedParamBuilderTest setRealNameExt1(String realName ) {
		this.combinedParamCheckor.setRealNameExt1(realName);
		return this;
	}
	
	//冗余名字2
	public CombinedParamBuilderTest setRealNameExt2(String realName ) {
		this.combinedParamCheckor.setRealNameExt2(realName);
		return this;
	}
	
	//昵称
	public CombinedParamBuilderTest setNickName(String nickName ) {
		this.combinedParamCheckor.setNickName(nickName);
		return this;
	}
	
	//冗余昵称1
	public CombinedParamBuilderTest setNickNameExt1(String nickName ) {
		this.combinedParamCheckor.setNickNameExt1(nickName);
		return this;
	}
	
	//冗余昵称2
	public CombinedParamBuilderTest setNickNameExt2(String nickName ) {
		this.combinedParamCheckor.setNickNameExt2(nickName);
		return this;
	}
	
	//手机号
	public CombinedParamBuilderTest setMobile(String mobile ) {
		this.combinedParamCheckor.setMobile(mobile);
		return this;
	}
	
	//冗余手机号1
	public CombinedParamBuilderTest setMobileExt1(String Mobile ) {
		this.combinedParamCheckor.setMobileExt1(Mobile);
		return this;
	}
	
	//冗余手机号2
	public CombinedParamBuilderTest setMobileExt2(String Mobile ) {
		this.combinedParamCheckor.setMobileExt2(Mobile);
		return this;
	}
	
	//省编号
	public CombinedParamBuilderTest setProvinceNo(String provinceNo ) {
		this.combinedParamCheckor.setProvinceNo(provinceNo);
		return this;
	}
	//市编号
	public CombinedParamBuilderTest setCityNo(String cityNo ) {
		this.combinedParamCheckor.setCityNo(cityNo);
		return this;
	}
	
	//区编号
	public CombinedParamBuilderTest setDistrictNo(String districtNo ) {
		this.combinedParamCheckor.setDistrictNo(districtNo);
		return this;
	}
	
	//街道编号
	public CombinedParamBuilderTest setStreetNo(String streetNo ) {
		this.combinedParamCheckor.setStreetNo(streetNo);
		return this;
	}
	
	//生日
	public CombinedParamBuilderTest setBirthday(String birthday ) {
		this.combinedParamCheckor.setBirthday(birthday);
		return this;
	}
	
	//性别
	public CombinedParamBuilderTest setGender(String gender ) {
		this.combinedParamCheckor.setGender(gender);
		return this;
	}
	
	//整数
	public CombinedParamBuilderTest setIntegerJ(String number ) {
		this.combinedParamCheckor.setIntegerJ(number);
		return this;
	}
	
	//年龄
	public CombinedParamBuilderTest setAge(String age ) {
		this.combinedParamCheckor.setAge(age);
		return this;
	}
	
	//web地址
	public CombinedParamBuilderTest setWebUrl(String url ) {
		this.combinedParamCheckor.setWebUrl(url);
		return this;
	}
	
	//电子邮箱
	public CombinedParamBuilderTest setEmail(String email ) {
		this.combinedParamCheckor.setEmail(email);
		return this;
	}
	
	public CombinedParamBuilderTest() {
		this.combinedParamCheckor = new CombinedParamCheckor();
	}

	public CombinedParamCheckor build() {
		return this.combinedParamCheckor;
	}
}
