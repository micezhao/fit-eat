package com.f.a.kobe.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.manager.CustomerBaseInfoManager;
import com.f.a.kobe.pojo.CustomerBaseInfo;
import com.f.a.kobe.pojo.enums.DrEnum;
import com.f.a.kobe.pojo.enums.LoginTypeEnum;
import com.f.a.kobe.util.DateUtils;
import com.f.a.kobe.util.IdWorker;

@Service
public class CustomerBaseInfoService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerBaseInfoService.class);

	@Autowired
	CustomerBaseInfoManager customerBaseInfoManager;

	@Autowired
	IdWorker idworker;

	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public void insertCustomerBaseInfo(CustomerBaseInfo customerBaseInfo) {
		customerBaseInfo.setCustomerId(idworker.nextId());
		if(StringUtils.isNotBlank(customerBaseInfo.getBirthday()) ) {
			customerBaseInfo.setBirthday(customerBaseInfo.getBirthday());
			customerBaseInfo.setAge(DateUtils.sumAge(customerBaseInfo.getBirthday()));
		}
		if(StringUtils.isNotBlank(customerBaseInfo.getMobile() )) {
			customerBaseInfo.setMobile(customerBaseInfo.getMobile());
		}
		if(StringUtils.isNotBlank(customerBaseInfo.getRealname())) {
			customerBaseInfo.setRealname(customerBaseInfo.getRealname());
		}
		if(StringUtils.isNotBlank(customerBaseInfo.getGender())) {
			customerBaseInfo.setGender(customerBaseInfo.getGender());
		}
		if(StringUtils.isNotBlank(customerBaseInfo.getNickname()) ) {
			customerBaseInfo.setNickname(customerBaseInfo.getNickname());
		}
		if(StringUtils.isNotBlank(customerBaseInfo.getHeadimg()) ) {
			customerBaseInfo.setHeadimg(customerBaseInfo.getHeadimg());
		}
		customerBaseInfo.setDr(DrEnum.AVAILABLE.getCode());
		customerBaseInfoManager.insert(customerBaseInfo);
	}

	/**
	 * 切换用户的状态
	 * 
	 * @param customerId 用户编号
	 * @param tragerDr   目标状态
	 */
	public void switchCustomerStates(Long customerId, DrEnum tragerDr) {
		CustomerBaseInfo customerInfo = customerBaseInfoManager.queryByBiz(customerId);
		customerInfo.setDr(tragerDr.getCode());
		customerBaseInfoManager.update(customerInfo);
		logger.info("用户{},状态更新成功，当前状态为:{}", customerInfo.getCustomerId(),
				DrEnum.getByCode(customerInfo.getDr()).getDescription());
	}

	// 修改用户基本信息
	public void updateCustomer(CustomerBaseInfo customerBaseInfo) {
		 if(StringUtils.isNotBlank(customerBaseInfo.getBirthday())) {
			int age =  DateUtils.sumAge(customerBaseInfo.getBirthday());
			customerBaseInfo.setAge(age);
		 }
		customerBaseInfoManager.update(customerBaseInfo);
	}
	
	// 查询用户信息
	public  CustomerBaseInfo query(Long customerId) {
		return customerBaseInfoManager.queryByBiz(customerId);
	}
	
	// 查询用户是否存在
	public boolean exsisted(Long customerId) {
		return customerBaseInfoManager.queryByBiz(customerId)==null?false:true;
	}
	
	public void delete(Long id) {
		customerBaseInfoManager.delete(id);
	}
	
	// 查询用户是否存在
	public boolean hasBinded(Long customerId,String mobile) {
		CustomerBaseInfo conditional = new CustomerBaseInfo();
		conditional.setCustomerId(customerId);
		conditional.setMobile(mobile);
		List<CustomerBaseInfo> list = customerBaseInfoManager.listByConditional(conditional);
		return list.isEmpty() == true ? false : true;
	}

	/**
	 * 批量更新用户的年龄
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void updateCustomerAgeTask() throws InterruptedException, ExecutionException {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateStr = sdf.format(now);
		CustomerBaseInfo condition = new CustomerBaseInfo();
		condition.setBirthday(currentDateStr);
		List<CustomerBaseInfo> list = customerBaseInfoManager.listByConditional(condition);
		List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();
		if (list.size() == 0) {
			return;
		}
		int threadNum = list.size() / 500;
		if (list.size() % 500 != 0) {
			threadNum = threadNum + 1;
		}
		List<CustomerBaseInfo> tempList = null;
		int n = 0; // 计数器
		for (int i = 1; i <= threadNum; i++) {
			tempList = new ArrayList<CustomerBaseInfo>();
			if (i == 1) {
				tempList.addAll(list.subList(n * 500, i * 500));
			} else {
				if(i == threadNum) {
					tempList.addAll(list.subList(n * 500+1,list.size() ));
					logger.info("末班车开起来～～～～嘟嘟嘟～～～");
				}else {
					tempList.addAll(list.subList(n * 500 + 1, i * 500));
				}
			}
			Future<Boolean> f = task(tempList);
			futureList.add(f);
			n++;
		}
		for (Future<Boolean> resultList : futureList) {
			resultList.get();
		}

	}

	private Future<Boolean> task(List<CustomerBaseInfo> list) {
		return threadPoolTaskExecutor.submit(new TaskHanler(list, customerBaseInfoManager));
	}

	// 批量更新用户的年龄
	private class TaskHanler implements Callable<Boolean> {

		private CustomerBaseInfoManager manager;

		private List<CustomerBaseInfo> infos;

		public TaskHanler(List<CustomerBaseInfo> infos, CustomerBaseInfoManager manager) {
			this.infos = infos;
			this.manager = manager;
		}

		@Override
		public Boolean call() throws Exception {
			execute(infos);
			return true;
		}

		public void execute(List<CustomerBaseInfo> infos) {
			CustomerBaseInfo curInfo = null;
			for (CustomerBaseInfo item : infos) {
				curInfo = new CustomerBaseInfo();
				curInfo.setAge(item.getAge() + 1);
				manager.update(curInfo);
			}
		}
	}

	

}
