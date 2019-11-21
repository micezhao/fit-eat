package com.f.a.kobe.service.impl.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.bo.AuthResult;
import com.f.a.kobe.pojo.enums.DrEnum;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.service.CustomerCredentialService;
import com.f.a.kobe.service.MobileValidateCodeService;

@Component("appCustomerCredentialService")
public class AppCustomerCredentialService extends CustomerCredentialService {
	
	@Autowired
	MobileValidateCodeService mobileValidateCodeService;
	
	@Override
	protected CustomerCredential queryCustomerCredential(String authCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthResult getAuthInfoByLoginRequest(ParamRequest requestAuth) {
		String username = requestAuth.getUsername();
		String password = requestAuth.getPassword();
		AuthResult authResult;
		if(StringUtils.isEmpty(username)) {
			authResult = mobileValidateCodeLogin(requestAuth.getMobile(),requestAuth.getValidateCode());
		}else {
			authResult = userNameAndPasswordLogin(username,password);
		}
		return authResult;
	}

	private AuthResult mobileValidateCodeLogin(String mobile, String validateCode) {
		if(mobileValidateCodeService.checkMobileValidateCode(mobile, validateCode)) {
			CustomerCredential conditional = new CustomerCredential();
			conditional.setMobile(mobile);
			List<CustomerCredential> result = manager.listByConditional(conditional);
			if(result == null) {
				throw new InvaildException(ErrEnum.CUSTOMER_NOT_FOUND.getErrCode(),ErrEnum.CUSTOMER_NOT_FOUND.getErrMsg());
			}
			CustomerCredential customerCredential = result.get(0);
			AuthResult authResult = new AuthResult();
			authResult.setCustomerId(customerCredential.getCustomerId());
			return authResult;
		}
		return null;
	}

	private AuthResult userNameAndPasswordLogin(String username,String password) {
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
		CustomerCredential customerCredential = new CustomerCredential();
		String username = request.getUsername();
		String password = request.getPassword();
		String mobile = request.getMobile();
		customerCredential.setCustomerId(idWorker.nextId());
		customerCredential.setUsername(username);
		customerCredential.setPassword(password);
		customerCredential.setDr(DrEnum.AVAILABLE.getCode());
		customerCredential.setMobile(mobile);
		manager.insert(customerCredential);
		
	}

}
