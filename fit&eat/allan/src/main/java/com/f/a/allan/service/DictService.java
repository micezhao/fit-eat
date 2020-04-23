package com.f.a.allan.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.bo.DictBo;
import com.f.a.allan.entity.pojo.Dict;
import com.f.a.allan.enums.DrEnum;
import com.f.a.allan.utils.ObjectUtils;

@Service
public class DictService {
	
	@Autowired
	private MongoTemplate template;
	
	private Dict dict = new Dict();
	
	
	public Dict getById(String id) {
		return template.findById(id, dict.getClass());
	}
	
	private String now() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
	
	/**
	 * 新增一条配置记录，默认为可用状态
	 * @param dictBo
	 * @return
	 */
	public Dict insert(DictBo dictBo) {
		ObjectUtils.copy(dict, dictBo);
		dict.setStatus(DrEnum.AVAILABLE.getCode());
		dict.setCdt(now());
		return template.insert(dict);
	}
	
	public boolean removeById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return template.remove(query, new Dict().getClass()).wasAcknowledged();
	}
	
	public boolean update(DictBo dictBo) {
		ObjectUtils.copy(dict, dictBo);
		dict.setMdt(now());
		Query query = new Query(new Criteria().where("_id").is(dict.getId()));  // 封装查询条件，以找到需要被更新的对象
		Update update= ObjectUtils.getUpdateFields(dict);
		Long modifiedCount = template.updateFirst(query, update, "dict").getModifiedCount();
		return modifiedCount >= 1 ? true:false;
	}
	
	public boolean switchStatus(String id,String status) {
		DictBo dictBo = new DictBo();
		dictBo.setId(id);
		dictBo.setStatus(status);
		return this.update(dictBo);
	}
	
	public List<Dict> listByCondition(DictBo bo){
		Query query = new Query();  // 封装查询条件，以找到需要被更新的对象
		Criteria criteria = new Criteria();
		if(StringUtils.isNotBlank(bo.getId())) {
			criteria.where("id").is(bo.getId());
		}
		if(StringUtils.isNotBlank(bo.getGroup())) {
			criteria.where("group").is(bo.getGroup());
		}
		if(StringUtils.isNotBlank(bo.getKeyName())) {
			criteria.where("keyName").is(bo.getKeyName());
		}
		if(StringUtils.isNotBlank(bo.getStatus())) {
			criteria.where("status").is(bo.getStatus());
		}		
		query.limit(bo.getPageSize());
		query.skip((bo.getPageSize()-1)*bo.getPageSize());
		return (List<Dict>) template.find(query, new Dict().getClass());
		
	}

	
}
