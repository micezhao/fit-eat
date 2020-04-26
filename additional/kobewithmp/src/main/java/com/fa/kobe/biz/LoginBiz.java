package com.fa.kobe.biz;

import com.fa.kobe.customer.pojo.Credential;
import com.fa.kobe.customer.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginBiz {

    @Autowired
    private CredentialService credentialService;

    public void createCredential(Credential credential){
        credentialService.save(credential);
    }

    public Credential getCredentialById(Long id){
        return credentialService.getById(id);
    }
}
