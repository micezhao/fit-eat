package com.f.a.kobe.service;

import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.manager.CustomerAddrManager;
import com.f.a.kobe.pojo.CustomerAddr;
import com.f.a.kobe.pojo.enums.UseDefaultEnum;
import com.f.a.kobe.util.IdWorker;

@Service
public class CustomerAddrService {
	
	@Autowired
	private CustomerAddrManager manager;
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private RegionService regionService;
	
	public void insertAddr(CustomerAddr customerAddr) {
		Long customerId=customerAddr.getCustomerId();
		List<CustomerAddr> list = manager.listByCustomerId(customerId);
		if(list.size() > 8) {
			throw new InvaildException(ErrEnum.OVER_LIMITS.getErrCode(),
						"当前用户的收货地址"+ErrEnum.OVER_LIMITS.getErrMsg());
		}
		boolean hasDefault = false; 
		for (CustomerAddr item : list) {
			if(StringUtils.equals(UseDefaultEnum.USE_DEFAULT.getCode(), item.getUseDefault())) {
				hasDefault = true; // 如果发现有默认的地址就break；
				break;
			}
		}
		// 如果不存在默认地址，就将当前地址设为默认
		if(!hasDefault) {
			customerAddr.setUseDefault(UseDefaultEnum.USE_DEFAULT.getCode());
		}
		
		customerAddr.setAddrId(idWorker.nextId()); //生成一个序列号
		String provinceName = regionService.getAreaName(customerAddr.getProvinceNo());
		customerAddr.setProvinceName(provinceName);
		String cityName = regionService.getAreaName(customerAddr.getCityNo());
		customerAddr.setCityName(cityName);
		String distrcName = regionService.getAreaName(customerAddr.getDistrcNo());
		customerAddr.setDistrcName(distrcName);
		StringBuffer areaDetailBuffer = new StringBuffer();
		areaDetailBuffer.append(provinceName).append(cityName).append(distrcName).append(customerAddr.getAddrDetail());
		customerAddr.setAddrDetail(areaDetailBuffer.toString());
		manager.insert(customerAddr);
	}
	
	public void deleteAddr(Long id) {
		manager.delete(id);
	}
	
	/**
	 * 设置用户
	 * @param customerId
	 * @param addrId
	 */
	public void setDefaultAddr(Long customerId,Long addrId) {
		List<CustomerAddr> customerAddrList = manager.listByCustomerId(customerId);
		// 先查询这个用户的所有地址
		for (CustomerAddr item : customerAddrList) {
			//再将之前的默认地址清空
			if(StringUtils.equals(UseDefaultEnum.USE_DEFAULT.getCode(), item.getUseDefault()) ) {
				item.setUseDefault(null);
				manager.update(item);
			}
		}
		//再将这个新地址设为默认
		CustomerAddr currentAddr=manager.queryByBiz(addrId);
		currentAddr.setUseDefault(UseDefaultEnum.USE_DEFAULT.getCode());
		manager.update(currentAddr);	
	}
	
	/**
	 * 获取用户的收货地址
	 * TODO 先从mongo中获取，如果找不到在从sql中获取
	 * @param customerId
	 * @return
	 */
	public List<CustomerAddr> getCustomerAddrs(Long customerId){
		return manager.listByCustomerId(customerId);
	}
	
	/**
	 * 获取收货地址
	 * @param addrId
	 * @return
	 */
	public CustomerAddr getCustomerAddr(Long addrId) {
		// TODO 先从mongo中获取，如果找不到在从sql中获取
		return manager.queryByBiz(addrId);
	}
	
}
