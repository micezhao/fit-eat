package com.fa.kater.customer.pojo.bo;

import com.fa.kater.customer.pojo.Credential;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParamRequest extends Credential {

    /**
	 * 
	 */
	private static final long serialVersionUID = -693472468809889664L;

	private String loginType;

    private String code;

}
