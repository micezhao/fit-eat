package com.fa.kater.customer.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxAuthRtn {

    private String access_token;

    private String expires_in;

    private String refresh_token;

    private String openid;

    private String scope;

}
