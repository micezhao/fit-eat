package com.f.a.kobe.service.previous;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.manager.previous.CustomerBodyInfoManager;
import com.f.a.kobe.pojo.previous.CustomerBodyInfo;
import com.f.a.kobe.pojo.response.chart.LineChart;
import com.f.a.kobe.pojo.response.chart.WeightChart;
import com.f.a.kobe.pojo.view.CustomerBodyInfoView;

@Service
public class CustomerBodyInfoService {
	
	@Autowired
	private CustomerBodyInfoManager manager;
	
	private static final String COLLECTION_NAME = "CustomerBodyInfoView";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private String recordIdBuilder(Long customerId,String preiffxRecordId) {
		String curDate = sdf.format(Calendar.getInstance().getTime()).replaceAll("-", "");
		StringBuffer buffer = new StringBuffer(curDate);
		if(preiffxRecordId == null ) {
			buffer.append("00");
			return buffer.toString();
		}else {
			Long nextRecord =Long.valueOf(preiffxRecordId)+1;
			return String.valueOf(nextRecord);
		}
	}
	
	/**
	 * 注册一条信息的体征记录
	 * @param customerBodyInfo
	 */
	public CustomerBodyInfoView registBodyInfo(CustomerBodyInfo customerBodyInfo,String gender) {
		CustomerBodyInfo registedRecord = hasRegisted(customerBodyInfo.getCustomerId());
		if(registedRecord != null) { //如果存在就先删除
			delete(registedRecord);
			String recordId = recordIdBuilder(customerBodyInfo.getCustomerId(),registedRecord.getRecordId());
			customerBodyInfo.setRecordId(recordId);
			insert(customerBodyInfo);
		}else {			
			String recordId = recordIdBuilder(customerBodyInfo.getCustomerId(),null);
			customerBodyInfo.setRecordId(recordId);
			insert(customerBodyInfo);
		}
		CustomerBodyInfoView view = new CustomerBodyInfoView().build(customerBodyInfo, gender);
		// 使用 mongoTemplate.insert 方法，如果存在相同ID，则程序报错
		// 使用 mongoTemplate.save 方法，如果存在相同ID，则修改原先的数据，需要遍历整个集合，效率低
		mongoTemplate.insert(view, COLLECTION_NAME);
		return view;
	}
	

	
	private void insert(CustomerBodyInfo customerBodyInfo) {
		BigDecimal height = new BigDecimal(customerBodyInfo.getHeight());
		BigDecimal weight = new BigDecimal( customerBodyInfo.getWeight());
		// bim = 体重kg/身高(m)的二次幂 
		BigDecimal w = (height.divide(new BigDecimal(100))).pow(2);
 		BigDecimal bmi = weight.divide(w,2,BigDecimal.ROUND_HALF_UP);
		customerBodyInfo.setBmi(bmi.toString());
		customerBodyInfo.setRegisterDate(sdf.format(Calendar.getInstance().getTime()));
		BigDecimal hipline = new BigDecimal(customerBodyInfo.getHipline()); // 臀围
		BigDecimal waistline = new BigDecimal(customerBodyInfo.getWaistline());//腰围
		BigDecimal waistHipRatio = waistline.divide(hipline,2,BigDecimal.ROUND_HALF_UP);
		customerBodyInfo.setWaistHipRatio(waistHipRatio.toString()); // 腰臀比
		manager.insert(customerBodyInfo);
	}
	
	private boolean delete(CustomerBodyInfo customerBodyInfo) {
		Query query = new Query(Criteria.where("_id").is(customerBodyInfo.getId()));
		manager.delete(customerBodyInfo.getId());
		boolean isAck = mongoTemplate.remove(query, COLLECTION_NAME).wasAcknowledged();
		return isAck;
		
	}
	
	
	//检查当前用户是否已经在当日登记过体征信息的方法
	public CustomerBodyInfo hasRegisted(Long customerId) {
		CustomerBodyInfo conditional = new CustomerBodyInfo();
		conditional.setCustomerId(customerId);
		conditional.setRegisterDate(sdf.format(Calendar.getInstance().getTime()));
		List<CustomerBodyInfo> list = manager.listByConditional(conditional);
		if(list.isEmpty()) {
			return null;
		}else {
			if(list.size() > 1) {
				throw new InvaildException(ErrEnum.REDUPICATE_RECORD.getErrCode(),ErrEnum.REDUPICATE_RECORD.getErrMsg());
			}else {
				return list.get(0);
			}
		}
	}
	
	/**
	 * 获取用户体重信息的图表数据 堆叠柱状图
	 * @param customerId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<WeightChart> getWeightChart(Long customerId,String startDate,String endDate){
		Query query = new Query();
		query.addCriteria(Criteria.where("customerId").is(customerId)) ;
		query.addCriteria(Criteria.where("registerDate").gte(startDate).lte(endDate));
		return mongoTemplate.find(query, WeightChart.class, COLLECTION_NAME);
	}
	
	public List<LineChart> getLineChart(Long customerId,String startDate,String endDate){
		Query query = new Query();
		query.addCriteria(Criteria.where("customerId").is(customerId)) ;
		query.addCriteria(Criteria.where("registerDate").gte(startDate).lte(endDate));
		return mongoTemplate.find(query, LineChart.class, COLLECTION_NAME);
	}
	
	

}
