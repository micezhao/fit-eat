package com.f.a.allan.dao.mongo;

import java.util.List;

public interface MongoBaseMapper<T> {
	
	public void insert(T t);
	
	public void saveAndReplace(T t);
	
	public List<T> findList(T t);
	
	public void delete (T t);
}
