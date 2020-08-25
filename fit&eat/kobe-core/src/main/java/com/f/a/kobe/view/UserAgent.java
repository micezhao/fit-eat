package com.f.a.kobe.view;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserAgent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3035675799721442471L;
	
	private String agentId;
	
	private String loginType;
	
	private String authType;
	
	private String merchantId;
	/**
	 * 用户账号
	 */
	private String userAccount;

	/**
	 * 关联手机号
	 */
	private String mobile;

	/**
	 * 微信凭证
	 */
	private String wxopenid;

	/**
	 * 支付宝凭证
	 */
	private String aliopenid;

	/**
	 * 用户类型
	 */
	private String userType;

	/**
	 * 登录密码
	 */
	private String loginPwd;

	/**
	 * 真实姓名
	 */
	private String realname;

	/**
	 * 出生日期
	 */
	private String birthday;

	/**
	 * 年龄
	 */
	private Integer age;

	/**
	 * 性别
	 */
	private String gender;

	/**
	 * 用户昵称
	 */
	private String nickname;

	/**
	 * 用户头像
	 */
	private String headimgUrl;

	/**
	 * 当前积分
	 */
	private Integer score;
	
	/**
	 * 是否绑定了手机号
	 */
	private boolean hasbinded;


}
