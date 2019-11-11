package com.f.a.kobe.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.f.a.kobe.exceptions.ErrCodeEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.manager.CustomerBaseInfoManager;
import com.f.a.kobe.pojo.CustomerBaseInfo;
import com.f.a.kobe.pojo.DrEnum;
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
		customerBaseInfo.setId(idworker.nextId());
		customerBaseInfo.setAge(sumAge(customerBaseInfo.getBirthday()));
		customerBaseInfo.setDr(DrEnum.AVAILABLE.getCode());
		customerBaseInfoManager.insert(customerBaseInfo);
	}
	
	/**
	 * 切换用户的状态
	 * @param customerId 用户编号
	 * @param tragerDr 目标状态
	 */
	public void switchCustomerStates(Long customerId,DrEnum tragerDr) {
		CustomerBaseInfo customerInfo = customerBaseInfoManager.queryByBiz(customerId);
		customerInfo.setDr(tragerDr.getCode());
		customerBaseInfoManager.update(customerInfo);
		logger.info("用户{},状态更新成功，当前状态为:{}", 
				customerInfo.getCustomerId(),
				DrEnum.getByCode(customerInfo.getDr()).getDescription());
	}
	
	// 修改用户信息
	public void updateCustomer(CustomerBaseInfo customerBaseInfo) {
		customerBaseInfoManager.update(customerBaseInfo);
	}
	
	
	/**
	 * 批量更新用户的年龄
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
		List <Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();
		if(list.size()==0) {
			return ;
		}
		int threadNum  = list.size()/500;
		if(list.size()%500 != 0) {
			threadNum = threadNum +1;
		}
		List<CustomerBaseInfo> tempList = null;
		int n = 0; //计数器
		for (int i = 1; i <= threadNum; i++) {
			tempList = new ArrayList<CustomerBaseInfo>();
			if(i == 1) {
				tempList.addAll(list.subList(n*500, i*500));
			}else {
				tempList.addAll(list.subList(n*500+1, i*500));
			}
			Future<Boolean> f= task(tempList);
			futureList.add(f);
			n++;
		}
		for (Future<Boolean> resultList : futureList) {
			resultList.get();
		}
		
 		
	}
	
	public Future<Boolean> task(List<CustomerBaseInfo> list) {
		return threadPoolTaskExecutor.submit(new TaskHanler(list, customerBaseInfoManager));
	}
	
	// 批量更新用户的年龄
	public class TaskHanler implements Callable<Boolean>{
		
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
	
	
	private int sumAge(String csny) {
		// 根据出生年月求年龄
		// 根据出生年月求年龄 适用于 2018-1-1 类似的日期
		// 解析出生年月
		String[] split = csny.split("-");
		if (split.length != 3) {
			// 给的出生年月不合法
			throw new InvaildException(ErrCodeEnum.INPUT_PARAM_INVAILD.getErrCode(), "出生日期格式不正确");
		}
		String birthMonth = split[1];
		if(split[1].startsWith("0",0)) {
			birthMonth = split[1].replaceFirst("0", "");
		}
		String birthDay = split[2];
		if(split[1].startsWith("0",0)) {
			birthDay = split[2].replaceFirst("0", "");
		}
		// 求当前年月日
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		// 算出当前大概年龄
		int age = year - Integer.parseInt(split[0]);
		if (age < 0) {
			throw new InvaildException(ErrCodeEnum.INPUT_PARAM_INVAILD.getErrCode(), "出生年份不能小于当前年份");
		}
		if (month - Integer.parseInt(birthMonth) > 0) {
			// 过生日了
		} else if (month - Integer.parseInt(birthMonth) == 0) {
			// 在本月，不知道有没有过生日，需要用天去判断
			if (day - Integer.parseInt(birthDay) < 0) {
				// 没有过生日
				age -= 1;
			}
		} else {
			// 没有过生日
			age -= 1;
		}
		return age;
	}
	
}
