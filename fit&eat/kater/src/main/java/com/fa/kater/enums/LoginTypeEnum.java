package com.fa.kater.enums;

/**
 * 登录渠道的枚举类
 * @author micezhao
 *
 */
public enum LoginTypeEnum {

    WECHAT("wx_lite","微信登陆"),
    ALI_PAY("ali_lite","支付宝"),
    WEB("web","网页登录"),
    APP("app","app");
	
    public String type;	
    public String desc;
   
    private LoginTypeEnum(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}
    
	public static LoginTypeEnum getLoginTypeEnum(String type) {
        LoginTypeEnum[] values = LoginTypeEnum.values();
        for(LoginTypeEnum value : values) {
            if(value.type.equalsIgnoreCase(type)) {
                return value;
            }
        }
        return null;
    }
}
