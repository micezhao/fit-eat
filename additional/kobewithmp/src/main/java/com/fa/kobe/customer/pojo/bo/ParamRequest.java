package com.fa.kobe.customer.pojo.bo;

import com.fa.kobe.customer.pojo.Credential;
import lombok.Data;

@Data
public class ParamRequest extends Credential {

    private String loginType;

    private String code;

}
