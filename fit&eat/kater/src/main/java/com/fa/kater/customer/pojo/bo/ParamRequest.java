package com.fa.kater.customer.pojo.bo;

import com.fa.kater.customer.pojo.Credential;
import lombok.Data;

@Data
public class ParamRequest extends Credential {

    private String loginType;

    private String code;

}
