package com.fa.kobe.customer.pojo.bo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class UserAgent implements Serializable {

    private String loginType;


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
     * 用户手机号
     */
    private String updateMobile;
}
