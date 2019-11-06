package com.f.a.kobe.pojo.View;

import lombok.Data;

@Data
public class UserAgent {
		// 用户编号
	  	private Long customerId;
	  	// 真是姓名
	    private String realname;
	    // 生日
	    private String birthday;
	    // 年龄
	    private Integer age;
	    // 昵称
	    private String nickname;
	    // 头像地址
	    private String headimg;
	    //当前积分
	    private Integer sorce;
	    // 联系方式
	    private String mobile;
	    // 微信id
	    private String wxOpenid;
	    // 阿里id
	    private String aliOpenid;   
	    // 登陆用户名
	    private String username;
	    // 登陆密码
	    private String password;
	    // 邮箱信息
	    private String email;
	
}
