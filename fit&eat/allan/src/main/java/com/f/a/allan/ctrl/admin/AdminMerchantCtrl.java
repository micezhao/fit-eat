package com.f.a.allan.ctrl.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.biz.MerchantBiz;
import com.f.a.allan.entity.pojo.Merchant;
import com.f.a.allan.entity.request.MerchantQueryRequest;
import com.f.a.allan.entity.request.MerchantQueryRequest.MerchantQueryRequestBuilder;

@RestController
@RequestMapping("/admin/merchant")
public class AdminMerchantCtrl {

	private final static String SYMBOL_AND = "&";

	@Autowired
	private MerchantBiz merchantBiz;

	@GetMapping("/name/{name}/verify/{verify}/" + "operation/{operation}/holderName/"
			+ "{holderName}/holderPhone/{holderPhone}")
	public ResponseEntity<Object> listMerchant(@PathVariable("name") String merchantName,
			@PathVariable("verify") String verifyStatus,
			@PathVariable("operation") String operationStatus,
			@PathVariable("holderName") String holderName,
			@PathVariable("holderPhone") String holderPhone) {
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
}
