package com.f.a.allan.dao.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * 用于连接mongodb的crud方法
 * @author micezhao
 *
 */

public class MongoBaseMapper<T> {
	
	@Autowired
	private MongoTemplate template;
	
	/**
	 * 
	 * @param t 
	 * @param collectionName 集合名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll(T t,String collectionName){
		return (List<T>) template.findAll(t.getClass(), collectionName);
	}
	
	@SuppressWarnings("unchecked")
	public T getById(T t,Long id) {
		if(id == null || id == 0) {
			return null;
		}
		Query query = new Query(Criteria.where("id").is(id));
		return (T) template.findOne(query, t.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public T getByBiz(Object bizId,String bizCol,T t) {
		Query query = new Query(Criteria.where("bizId").is(bizCol));
		return (T) template.findOne(query, t.getClass());
	}
	
	
	// 注意template.save 和 template.insert 的区别
	// insert不允许出现重复的主键，save操作时如果遇到重复的主键会执行更新
	// insert的执行效率要高于save，应为save时会遍历整个文档
	public void insert(T t) {
		 template.insert(t);
	}
	
	
	public void delete(T t) {
		 template.remove(t).wasAcknowledged();
	}
	

	
	
}
