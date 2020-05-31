package com.f.a.allan.ctrl.admin.merchant;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.biz.MerchantBiz;
import com.f.a.allan.ctrl.admin.BaseAdminCtrl;
import com.f.a.allan.entity.pojo.Merchant;
import com.f.a.allan.entity.request.MerchantQueryRequest;
import com.f.a.allan.entity.request.MerchantQueryRequest.MerchantQueryRequestBuilder;
import com.f.a.allan.entity.request.MerchantRequest;
import com.f.a.allan.enums.MerchantStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "admin-商户管理功能")
@RestController
@RequestMapping("/admin/merchant")
public class AdminMerchantCtrl extends BaseAdminCtrl{

	@Autowired
	private MerchantBiz merchantBiz;

	@ApiOperation("查询商户信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "商户名称", required = false),
			@ApiImplicitParam(name = "verify", value = "商户的认证状态", required = false),
			@ApiImplicitParam(name = "operation", value = "商户的运行状态", required = false),
			@ApiImplicitParam(name = "holderName", value = "商户负责人姓名", required = false),
			@ApiImplicitParam(name = "holderPhone", value = "商户负责人联系电话", required = false)})
	@GetMapping
	public ResponseEntity<Object> listMerchant(@RequestParam(name = "name", required = false) String merchantName,
			@RequestParam(name = "verify", required = false) String verifyStatus,
			@RequestParam(name = "operation", required = false) String operationStatus,
			@RequestParam(name = "holderName", required = false) String holderName,
			@RequestParam(name = "holderPhone", required = false) String holderPhone) {
		MerchantQueryRequestBuilder requestBuilder = MerchantQueryRequest.builder();
		List<String> statusList = null;
		if (StringUtils.isNotBlank(merchantName)) {
			requestBuilder.merchantName(merchantName);
		}
		if (StringUtils.isNotBlank(verifyStatus)) {
			statusList = new ArrayList<String>();
			if (StringUtils.contains(verifyStatus, SYMBOL_AND)) {
				String[] statusArray = verifyStatus.split(SYMBOL_AND);
				for (String s : statusArray) {
					if (s.trim().isEmpty()) {
						continue;
					}
					statusList.add(s);
				}
			} else {
				statusList.add(verifyStatus);
			}
			requestBuilder.verifyStatusList(statusList);
		}

		if (StringUtils.isNotBlank(operationStatus)) {
			statusList = new ArrayList<String>();
			if (StringUtils.contains(operationStatus, SYMBOL_AND)) {
				String[] statusArray = operationStatus.split(SYMBOL_AND);
				for (String s : statusArray) {
					if (s.trim().isEmpty()) {
						continue;
					}
					statusList.add(s);
				}
			} else {
				statusList.add(operationStatus);
			}
			requestBuilder.operationStatusList(statusList);
		}
		if (StringUtils.isNotBlank(holderName)) {
			requestBuilder.holderName(holderName);
		}
		if (StringUtils.isNotBlank(holderPhone)) {
			requestBuilder.holderPhone(holderPhone);
		}
		List<Merchant> list = merchantBiz.listMerchant(requestBuilder.build());
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}

	@ApiOperation("商户运行管理")
	@PutMapping("{id}/operate/{operation}")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "商户id", required = true),
			@ApiImplicitParam(name = "operation", value = "操作", required = true, allowableValues = "opening,suspension,closed,off") })
	public ResponseEntity<Object>  operate(@PathVariable("id") String id, @PathVariable("operation") String operation) {
		merchantBiz.operateById(id, operation);
		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
	
	@ApiOperation("商户认证管理")
	@PutMapping("{id}/verify/{operation}")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "商户id", required = true),
			@ApiImplicitParam(name = "operation", value = "操作", required = true,allowableValues = "un_verified,verified")})
	public ResponseEntity<Object> verify(@PathVariable("id") String id, @PathVariable("operation") String operation) {
		if(MerchantStatus.getEnumByCode(operation) ==MerchantStatus.VERIFIED ) {
			merchantBiz.approveApply(id);
		}else {
			merchantBiz.rejectApply(id);
		}
		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
	
	@ApiOperation("商户信息更新")
	@PutMapping
	public ResponseEntity<Object> update(@RequestBody MerchantRequest request) {
		
		Merchant item = merchantBiz.updateMerchantById(request);
		return new ResponseEntity<Object>(item, HttpStatus.OK);
	}
	
	@ApiOperation("商户申请开店")
	@PostMapping
	public ResponseEntity<Object> apply(@RequestBody MerchantRequest request) {
		Merchant item = merchantBiz.enterApply(request);
		return new ResponseEntity<Object>(item, HttpStatus.OK);
	}
	
}
