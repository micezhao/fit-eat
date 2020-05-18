package com.f.a.allan.entity.request;


import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper =  false)
public class DictRequest extends BaseQueryRequest {
	
	@NotBlank(groups = {DictUpdate.class},message = " id blank is not allowed")
	private String id;
	/**
	 * 业务组别
	 */
	@NotBlank(groups = {DictInsert.class},message = " group blank is not allowed")
	private String group;
	/**
	 * 组别细分
	 */
	@NotBlank(groups = {DictInsert.class},message = " key blank is not allowed")
	private String key;
	/**
	 * 键
	 */
	@NotBlank(groups = {DictInsert.class},message = " keyName blank is not allowed")
	private String keyName;
	/**
	 * 值
	 */
	@NotBlank(groups = {DictInsert.class},message = " value blank is not allowed")
	private String value;
	
	/**
	 * 排序码
	 */
	private String sort = "99";
	
	private String orderBy = "id desc";
	
	public interface DictInsert{};
	
	public interface DictUpdate{}
	
}
