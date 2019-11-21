package com.f.a.kobe.service.impl.app;

import java.util.List;

import org.springframework.stereotype.Component;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.bo.AuthResult;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.service.CustomerCredentialService;

@Component("appCustomerCredentialService")
public class AppCustomerCredentialService extends CustomerCredentialService {
	
	@Override
	protected CustomerCredential queryCustomerCredential(String authCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthResult getAuthInfoByLoginRequest(ParamRequest requestAuth) {
		String username = requestAuth.getUsername();
		String password = requestAuth.getPassword();
		CustomerCredential conditional = new CustomerCredential();
		conditional.setUsername(username);
		conditional.setPassword(password);
		List<CustomerCredential> result = manager.listByConditional(conditional);
		if(result == null) {
			throw new InvaildException(ErrEnum.CUSTOMER_NOT_FOUND.getErrCode(),ErrEnum.CUSTOMER_NOT_FOUND.getErrMsg());
		}
		CustomerCredential customerCredential = result.get(0);
		AuthResult authResult = new AuthResult();
		authResult.setCustomerId(customerCredential.getCustomerId());
		return authResult;
	}

	@Override
	public CustomerCredential existsed(AuthResult authInfoByLoginRequest) {
		CustomerCredential customerCredential = new CustomerCredential();
		customerCredential.setCustomerId(authInfoByLoginRequest.getCustomerId());
		 List<CustomerCredential> list= manager.listByConditional(customerCredential);
		 if(list.isEmpty()) {
			 return null;
		 }
		 if(list.size() > 1) {
			 throw new InvaildException(ErrEnum.REDUPICATE_RECORD.getErrCode(),"用户凭证"+ErrEnum.REDUPICATE_RECORD.getErrMsg());
		 }
		 return list.get(0);
	}

	@Override
	public long insertCustomerCredential(AuthResult authInfoByLoginRequest) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void registerCustomer(ParamRequest request) {
		// TODO Auto-generated method stub

	}

}
