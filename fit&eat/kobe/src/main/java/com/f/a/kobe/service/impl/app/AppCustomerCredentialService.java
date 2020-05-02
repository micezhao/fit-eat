package com.f.a.kobe.service.impl.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.bo.AuthBo;
import com.f.a.kobe.pojo.enums.DrEnum;
import com.f.a.kobe.pojo.previous.CustomerCredential;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.service.previous.CustomerCredentialService;
import com.f.a.kobe.service.previous.MobileValidateCodeService;

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
	public AuthBo getAuthInfoByLoginRequest(ParamRequest requestAuth) {
		String username = requestAuth.getUsername();
		String password = requestAuth.getPassword();
		AuthBo authResult;
		if(StringUtils.isEmpty(username)) {
			authResult = mobileValidateCodeLogin(requestAuth.getMobile(),requestAuth.getValidateCode());
		}else {
			authResult = userNameAndPasswordLogin(username,password);
		}
		return authResult;
	}

	private AuthBo mobileValidateCodeLogin(String mobile, String validateCode) {
		if(mobileValidateCodeService.checkMobileValidateCode(mobile, validateCode)) {
			CustomerCredential conditional = new CustomerCredential();
			conditional.setMobile(mobile);
			List<CustomerCredential> result = manager.listByConditional(conditional);
			if(result == null) {
				throw new InvaildException(ErrEnum.CUSTOMER_NOT_FOUND.getErrCode(),ErrEnum.CUSTOMER_NOT_FOUND.getErrMsg());
			}
			CustomerCredential customerCredential = result.get(0);
			AuthBo authResult = new AuthBo();
			authResult.setCustomerId(customerCredential.getCustomerId());
			return authResult;
		}
		return null;
	}

	private AuthBo userNameAndPasswordLogin(String username,String password) {
		CustomerCredential conditional = new CustomerCredential();
		conditional.setUsername(username);
		conditional.setPassword(password);
		List<CustomerCredential> result = manager.listByConditional(conditional);
		if(result == null) {
			throw new InvaildException(ErrEnum.CUSTOMER_NOT_FOUND.getErrCode(),ErrEnum.CUSTOMER_NOT_FOUND.getErrMsg());
		}
		CustomerCredential customerCredential = result.get(0);
		AuthBo authResult = new AuthBo();
		authResult.setCustomerId(customerCredential.getCustomerId());
		return authResult;
	}

	@Override
	public boolean existsed(AuthBo authInfoByLoginRequest) {
		CustomerCredential customerCredential = new CustomerCredential();
		customerCredential.setCustomerId(authInfoByLoginRequest.getCustomerId());
		 List<CustomerCredential> list= manager.listByConditional(customerCredential);
		 if(list.isEmpty()) {
			 return false;
		 }
		 return true;
	}

	@Override
	public CustomerCredential insertCustomerCredential(AuthBo authInfoByLoginRequest) {
		// TODO Auto-generated method stub
		return null;
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
