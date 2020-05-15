package com.f.a.allan.ctrl.client.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.allan.entity.request.UserAddressQueryRequest;
import com.f.a.allan.entity.request.UserAddressRequest;
import com.f.a.allan.utils.ObjectUtils;
import com.f.a.kobe.view.UserAgent;

@RestController
@RequestMapping("/userAddress")
public class UserAddressCtrl {

	@Autowired
	private UserAddressBiz userAddressBiz;

	@PostMapping
	public UserAddress addAddress(UserAgent userAgent, UserAddressRequest request) {
		request.setUserAccount(userAgent.getUserAccount());
		UserAddress userAddress = new UserAddress();
		ObjectUtils.copy(userAddress, request);
		userAddressBiz.insert(userAddress);
		return userAddress;
	}

	@PutMapping("/{id}")
	public UserAddress updateAddress(@PathVariable("id") String id, UserAgent userAgent, UserAddressRequest request) {
		request.setUserAccount(userAgent.getUserAccount());
		UserAddress replacement = new UserAddress();
		ObjectUtils.copy(replacement, request);
		userAddressBiz.updateById(replacement);
		return replacement;
	}

	@PutMapping("/setDefault/{id}")
	public UserAddress setDefault(@PathVariable("id") String id, UserAgent userAgent) {
		return userAddressBiz.setDefault(userAgent.getUserAccount(), id);
	}

	@PutMapping("/dr/{id}")
	public UserAddress drSwitch(@PathVariable("id") String id, UserAgent userAgent) {
		return userAddressBiz.switchDr(id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteAddress(@PathVariable("id") String id, UserAgent userAgent) {
		userAddressBiz.deleteById(id);
	}
	
	@GetMapping("/{merchantId}")
	public List<UserAddress> listAddress(@PathVariable("merchantId") String merchantId,UserAgent userAgent,UserAddressQueryRequest request){
		request.setUserAccount(userAgent.getUserAccount());
		request.setMerchantId(merchantId);
		return userAddressBiz.listUserAddress(request);
	}
	
	@GetMapping("/{id}")
	public UserAddress getAddressById(@PathVariable("id") String id,UserAgent userAgent,UserAddressQueryRequest request){
		return userAddressBiz.findById(id);
	}
	
}
