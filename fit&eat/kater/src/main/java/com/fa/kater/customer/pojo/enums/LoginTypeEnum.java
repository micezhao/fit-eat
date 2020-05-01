package com.fa.kater.customer.pojo.enums;

public enum LoginTypeEnum {

    WECHAT(1,"wx","微信登陆"),
    ALI_PAY(2,"ali","支付宝"),
    APP(3,"app","app");

    private Integer loginTypeNum;

    private String loginTypeCode;

    private String description;

    private LoginTypeEnum(Integer loginTypeNum,String loginTypeCode, String description) {
        this.loginTypeCode = loginTypeCode;
        this.description = description;
        this.loginTypeNum = loginTypeNum;
    }

    public Integer getLoginTypeNum() {
        return loginTypeNum;
    }

    public void setLoginTypeNum(Integer loginTypeNum) {
        this.loginTypeNum = loginTypeNum;
    }

    public String getLoginTypeCode() {
        return loginTypeCode;
    }

    public void setLoginTypeCode(String loginTypeCode) {
        this.loginTypeCode = loginTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static LoginTypeEnum getLoginTypeEnum(String loginTypeCode) {
        LoginTypeEnum[] values = LoginTypeEnum.values();
        for(LoginTypeEnum value : values) {
            if(value.loginTypeCode.equalsIgnoreCase(loginTypeCode)) {
                return value;
            }
        }
        return null;
    }
}
