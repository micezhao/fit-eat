package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 商户详情
 * @author micezhao
 * mongo对象
 */
@Data
@Document
public class MerchantDetail {
	
	 /**
     * 商户编号
     */
    private String merchantNo;
	
    private String description;
    
    private String logo_url;
    
    private Advertisement advertisements;
	
    private LocalDateTime cdt;
	
    private LocalDateTime mdt;

}
