package com.f.a.kobe.basemanager;

import java.util.List;

public interface CustomerBaseManager<T> {

	// 通过id查询
	public T queryObjectById(Long id);

	// 通过userId查询
	public T queryObjectByUserId(String userId);

	// 通过条件查询
	public List<T> queryListByConditional(T t);

	// 添加一条记录，返回影响行数
	public int insertObject(T t);

	// 修改一条记录，返回影响行数
	public int updateObject(T t);

	// 删除一条记录，返回影响行数
	public int deleteObject(T t);
}
