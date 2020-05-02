package com.f.a.kobe.manager.previous;

import java.util.List;
/**
 * 通用的orm操作接口
 * @author micezhao
 *
 * @param <T>
 */
public interface BaseManager<T> {

	// 通过物理主件id查询
	public T queryById(Long id);

	// 通过业务主键查询
	public T queryByBiz(Object bizId);

	// 通过条件查询
	public List<T> listByConditional(T t);

	// 添加一条记录，返回影响行数
	public int insert(T t);

	// 修改一条记录，返回影响行数
	public int update(T t);

	// 通过物理主件id查询
	public int delete(Long id);
}
