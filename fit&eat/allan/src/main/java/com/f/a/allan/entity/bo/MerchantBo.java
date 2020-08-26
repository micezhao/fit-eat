package com.f.a.allan.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantBo {
	
	private Long id;

    private String agentId;

    private String agentName;

    private String territory;

    private String agentAddr;


    private String agentHolderName;
    
    private String agentHolderContact;
    
    private String merchantName;
    
    private String merchantHolderName;
    
    private String merchantHolderContact;

    private String merchantIntroduce;

    private String merchantLogoImgUrl;
    
    private String merchantType;
    
    private String scope;

    private String state;
	
}
