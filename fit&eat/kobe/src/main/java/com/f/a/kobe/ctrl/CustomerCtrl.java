package com.f.a.kobe.ctrl;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.config.contants.SystemContanst;
import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.ErrRtn;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.CustomerAddr;
import com.f.a.kobe.pojo.CustomerBaseInfo;
import com.f.a.kobe.pojo.CustomerBodyInfo;
import com.f.a.kobe.pojo.bo.DateSelection;
import com.f.a.kobe.pojo.enums.UseDefaultEnum;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.pojo.response.chart.WeightChart;
import com.f.a.kobe.pojo.view.CustomerBodyInfoView;
import com.f.a.kobe.pojo.view.UserAgent;
import com.f.a.kobe.service.CustomerAddrService;
import com.f.a.kobe.service.CustomerBaseInfoService;
import com.f.a.kobe.service.CustomerBodyInfoService;
import com.f.a.kobe.service.aop.ParamCheck;
import com.f.a.kobe.util.DateUtils;
import com.f.a.kobe.util.ObjectTransUtils;

@RestController
@RequestMapping("/customer")
public class CustomerCtrl {

	private static final Logger logger = LoggerFactory.getLogger(CustomerCtrl.class);

	@Autowired
	private CustomerBaseInfoService customerBaseInfoService;

	@Autowired
	private CustomerAddrService addrService;
	
	@Autowired
	private CustomerBodyInfoService customerBodyInfoService;

	/**
	 * 获取用户的基础信息
	 * 
	 * @param userAgent
	 * @param customerId
	 * @return
	 */
	@GetMapping()
	public ResponseEntity<Object> getCustomerBaseInfo(UserAgent userAgent) {
		CustomerBaseInfo user = customerBaseInfoService.query(userAgent.getCustomerId());
		if (user == null) {
			return new ResponseEntity<Object>(
					new ErrRtn(ErrEnum.CUSTOMER_NOT_FOUND.getErrCode(), ErrEnum.CUSTOMER_NOT_FOUND.getErrMsg()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Object>(user, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Deprecated
	@PostMapping
	public ResponseEntity<Object> updateCustomerBaseInfo(@RequestBody ParamRequest request, UserAgent userAgent,
			HttpSession session) {
		CustomerBaseInfo customerBaseInfo = new CustomerBaseInfo();
		ObjectTransUtils.copy(customerBaseInfo, request);
		customerBaseInfoService.updateCustomer(customerBaseInfo);
		logger.info("用户基本信息完成,当前用户基本信息:{}", customerBaseInfo.toString());
		ObjectTransUtils.copy(userAgent, customerBaseInfo);
		session.setAttribute(SystemContanst.USER_AGENT, userAgent);
		logger.info("用户基本信息已经同步更新到session中", userAgent.toString());
		return new ResponseEntity<Object>(customerBaseInfo, HttpStatus.OK);
	}

	@GetMapping("/addr/")
	public ResponseEntity<Object> getCustomerAddr(UserAgent userAgent) {
		List<CustomerAddr> addrlist = addrService.getCustomerAddrs(userAgent.getCustomerId());
		return new ResponseEntity<Object>(addrlist, HttpStatus.OK);
	}

	@ParamCheck("addAddr")
	@PostMapping("/addr/add")
	public ResponseEntity<Object> addCustomerAddr(@RequestBody ParamRequest request, UserAgent userAgent,
			HttpSession session) {
		CustomerAddr customerAddr = new CustomerAddr();
		ObjectTransUtils.copy(customerAddr, request);
		customerAddr.setCustomerId(userAgent.getCustomerId());
		addrService.insertAddr(customerAddr);
		if (StringUtils.equals(UseDefaultEnum.USE_DEFAULT.getCode(), customerAddr.getUseDefault())) {
			userAgent.setDefaultAddrId(String.valueOf(customerAddr.getAddrId()));
			session.setAttribute(SystemContanst.USER_AGENT, userAgent);
		}
		return new ResponseEntity<Object>(customerAddr, HttpStatus.OK);
	}

	@PostMapping("/addr/update")
	public ResponseEntity<Object> updateCustomerAddr(@RequestBody ParamRequest request, UserAgent userAgent,
			HttpSession session) {
		CustomerAddr customerAddr = new CustomerAddr();
		// TODO 参数校验
		ObjectTransUtils.copy(customerAddr, request);
		addrService.updateSelectedAddr(customerAddr);
		return new ResponseEntity<Object>(customerAddr, HttpStatus.OK);
	}

	@PostMapping("/addr/setDefault")
	public ResponseEntity<Object> setDefaultAddr(@RequestBody ParamRequest request, UserAgent userAgent,
			HttpSession session) {
		Long addrId = request.getAddrId();
		if (addrId == null || addrId == 0) {
			return new ResponseEntity<Object>(
					new ErrRtn(ErrEnum.QUERY_PARAM_INVAILD.getErrCode(), ErrEnum.QUERY_PARAM_INVAILD.getErrMsg()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		addrService.setDefaultAddr(userAgent.getCustomerId(), addrId); // 切换默认地址
		userAgent.setDefaultAddrId(String.valueOf(addrId));
		session.setAttribute(SystemContanst.USER_AGENT, userAgent);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@PostMapping("/addr/delete")
	public ResponseEntity<Object> deleteAddr(@RequestBody ParamRequest request, UserAgent userAgent,
			HttpSession session) {
		addrService.deleteAddr(request.getId());
		try {
			CustomerAddr defaultAddr = addrService.getCustomerDefaultAddrs(userAgent.getCustomerId());
			userAgent.setDefaultAddrId(String.valueOf(defaultAddr.getAddrId()) );
			session.setAttribute(SystemContanst.USER_AGENT, userAgent);
		} catch (InvaildException ex) {
			return new ResponseEntity<Object>(new ErrRtn(ex.getErrCode(), ex.getErrMsg()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@PostMapping("/bodyInfo/add")
	public ResponseEntity<Object> getBodyInfo(@RequestBody ParamRequest request,UserAgent userAgent){
		CustomerBodyInfo customerBodyInfo = new CustomerBodyInfo();
		request.setCustomerId(userAgent.getCustomerId());
		ObjectTransUtils.copy(customerBodyInfo, request);
		CustomerBodyInfoView view = customerBodyInfoService.registBodyInfo(customerBodyInfo, request.getGender());
		return new ResponseEntity<Object>(view,HttpStatus.OK);
	}

	
}
