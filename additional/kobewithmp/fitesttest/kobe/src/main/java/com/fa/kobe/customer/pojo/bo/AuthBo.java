package com.fa.kobe.customer.pojo.bo;

import lombok.Data;

@Data
public class AuthBo {

    private String authToken;

    private String sessionKey;

    private String openid;

    private String authType;

    private String userAccount;

    private String mobile;
}
