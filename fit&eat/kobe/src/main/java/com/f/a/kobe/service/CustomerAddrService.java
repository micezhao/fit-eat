package com.f.a.kobe.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
		List<String> idList = new ArrayList<String>();
		idList.add(customerAddr.getProvinceNo());
		idList.add(customerAddr.getCityNo());
		idList.add(customerAddr.getDistrcNo());
		String streetNo = customerAddr.getStreetNo();
		if(!StringUtils.isEmpty(streetNo)) {
			customerAddr.setStreetName(regionService.getAreaName(streetNo));
			idList.add(streetNo);
		}
		String areaBefore = regionService.getAreaName(idList);
		areaDetailBuffer.append(areaBefore).append(customerAddr.getAddrDetail()); // 组合详细地址信息
		customerAddr.setAddrDetail(areaDetailBuffer.toString());
		manager.insert(customerAddr);
	}
	
	public void deleteAddr(Long id) {
		CustomerAddr selectedAddr = manager.queryById(id);
		//如果当前被删除的地址是默认地址，那么就需要从其他地址中一个为默认地址
		if(StringUtils.equals(UseDefaultEnum.USE_DEFAULT.getCode(), selectedAddr.getUseDefault())) {
			manager.delete(id); // 执行删除操作
			Long customerId = selectedAddr.getCustomerId() ;
			List<CustomerAddr> addrList = manager.listByCustomerId(customerId);
			addrList.sort(new Comparator<CustomerAddr>() {
				@Override //按照升序重排集合
				public int compare(CustomerAddr o1, CustomerAddr o2) {
					 int i = (int)(o1.getAddrId() - o2.getAddrId());  
		                return i;
				}
			});
			CustomerAddr optioned = addrList.get(0); //将 最近添加的一个地址选中，作为默认地址
			optioned.setUseDefault(UseDefaultEnum.USE_DEFAULT.getCode()); 
			manager.update(optioned);
		}else { // 否则就直接删除
			manager.delete(id);
		}
		
	}
	
	/**
	 * 用户设定默认地址
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
	 * 更新地址
	 * @param addr
	 */
	public void updateSelectedAddr(CustomerAddr addr) {
		// 判断是否需要将当前修改的记录设置为默认地址,
		if(StringUtils.equals(UseDefaultEnum.USE_DEFAULT.getCode(),addr.getUseDefault()) ) {
			//如果需要将它设置为默认地址，就先执行 设定默认地址的操作
			setDefaultAddr(addr.getCustomerId(), addr.getAddrId());
		}
		String provinceName = regionService.getAreaName(addr.getProvinceNo());
		addr.setProvinceName(provinceName);
		String cityName = regionService.getAreaName(addr.getCityNo());
		addr.setCityName(cityName);
		String distrcName = regionService.getAreaName(addr.getDistrcNo());
		addr.setDistrcName(distrcName);
		StringBuffer areaDetailBuffer = new StringBuffer();
		List<String> idList = new ArrayList<String>();
		idList.add(addr.getProvinceNo());
		idList.add(addr.getCityNo());
		idList.add(addr.getDistrcNo());
		String areaBefore = regionService.getAreaName(idList);
		areaDetailBuffer.append(areaBefore).append(addr.getAddrDetail()); // 组合详细地址信息
		addr.setAddrDetail(areaDetailBuffer.toString());
		manager.updateByBizId(addr);
	} 
	
	/**
	 * 获取用户的收货地址
	 * @param customerId
	 * @return
	 */
	public List<CustomerAddr> getCustomerAddrs(Long customerId){
		return manager.listByCustomerId(customerId);
	}
	
	/**
	 * 查询用户的默认地址
	 * @param customerId
	 * @return
	 */
	public CustomerAddr getCustomerDefaultAddrs(Long customerId) throws InvaildException{
		CustomerAddr conditional = new CustomerAddr();
		conditional.setCustomerId(customerId);
		conditional.setUseDefault(UseDefaultEnum.USE_DEFAULT.getCode());
		List<CustomerAddr> list = manager.listByConditional(conditional);
		if(list.isEmpty()) {
			throw new InvaildException(ErrEnum.NO_DEFAULT_ADDR.getErrCode(), ErrEnum.NO_DEFAULT_ADDR.getErrMsg());
		} 
		if(list.size() > 1) {
			throw new InvaildException(ErrEnum.REDUPICATE_RECORD.getErrCode(), ErrEnum.REDUPICATE_RECORD.getErrMsg());
		}
			
		return list.get(0);
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
