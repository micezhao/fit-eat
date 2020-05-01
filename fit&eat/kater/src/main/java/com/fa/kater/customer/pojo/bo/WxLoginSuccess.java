package com.fa.kater.customer.pojo.bo;

import lombok.Data;

@Data
public class WxLoginSuccess {

    private String session_key;

    private String openid;

}

