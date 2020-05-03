package com.fa.kater.customer.pojo.bo;

import com.fa.kater.customer.pojo.Credential;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamRequest extends Credential {

    /**
	 * 
	 */
	private static final long serialVersionUID = -693472468809889664L;

	private String loginType;

    private String code;

}
