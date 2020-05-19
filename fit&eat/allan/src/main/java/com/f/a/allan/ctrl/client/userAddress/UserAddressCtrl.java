package com.f.a.allan.ctrl.client.userAddress;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.allan.entity.request.UserAddressQueryRequest;
import com.f.a.allan.entity.request.UserAddressRequest;
import com.f.a.allan.utils.ObjectUtils;
import com.f.a.kobe.view.UserAgent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "client-用户收货地址管理")
@RestController
@RequestMapping("/userAddress")
public class UserAddressCtrl {

	@Autowired
	private UserAddressBiz userAddressBiz;
	
	@ApiOperation(value = "新增收货地址", notes="在指定商户中新增一条收货地址" )
	@PostMapping
	public UserAddress addAddress(UserAgent userAgent,@RequestBody UserAddressRequest request) {
		request.setUserAccount(userAgent.getUserAccount());
		UserAddress userAddress = new UserAddress();
		ObjectUtils.copy(userAddress, request);
		userAddressBiz.insert(userAddress);
		return userAddress;
	}
	
	@ApiOperation(value = "根据Id更新收货地址信息")
	@PutMapping("/{id}")
	public UserAddress updateAddress(@PathVariable("id") String id, UserAgent userAgent, @RequestBody UserAddressRequest request) {
		request.setUserAccount(userAgent.getUserAccount());
		UserAddress replacement = new UserAddress();
		ObjectUtils.copy(replacement, request);
		userAddressBiz.updateById(replacement);
		return replacement;
	}
	
	@ApiOperation(value = "设为默认收货地址")
	@PutMapping("/setDefault/{id}")
	public UserAddress setDefault(@PathVariable("id") String id, UserAgent userAgent) {
		return userAddressBiz.setDefault(userAgent.getUserAccount(), id);
	}
	
	@ApiOperation(value = "收货地址可用状态")
	@PutMapping("/dr/{id}")
	public UserAddress drSwitch(@PathVariable("id") String id, UserAgent userAgent) {
		return userAddressBiz.switchDr(id);
	}
	
	@ApiOperation(value = "删除收货地址")
	@DeleteMapping("/{id}")
	public void deleteAddress(@PathVariable("id") String id, UserAgent userAgent) {
		userAddressBiz.deleteById(id);
	}
	
	@ApiOperation(value = "根据商户id查询收货地址")
	@GetMapping("/merchant/{merchantId}")
	public List<UserAddress> listAddress(@PathVariable("merchantId") String merchantId,UserAgent userAgent){
		UserAddressQueryRequest request = new UserAddressQueryRequest();
		request.setUserAccount(userAgent.getUserAccount());
		request.setMerchantId(merchantId);
		return userAddressBiz.listUserAddress(request);
	}
	
	@ApiOperation(value = "根据id查询收货地址")
	@GetMapping("/{id}")
	public UserAddress getAddressById(@PathVariable("id") String id,UserAgent userAgent){
		return userAddressBiz.findById(id);
	}
	
}
