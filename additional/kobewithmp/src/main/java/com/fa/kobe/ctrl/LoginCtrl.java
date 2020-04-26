package com.fa.kobe.ctrl;

import com.fa.kobe.biz.LoginBiz;
import com.fa.kobe.customer.pojo.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginCtrl {

    @Autowired
    LoginBiz loginBiz;

    @PostMapping("/credential")
    public void registe(@RequestBody Credential credential){
        loginBiz.createCredential(credential);
    }

    @GetMapping("/credential/{id}")
    public ResponseEntity<Credential> getCredential(@PathVariable(value="id") Long id){
        Credential credential = loginBiz.getCredentialById(id);
        return new ResponseEntity<Credential>(credential, HttpStatus.OK);
    }
}
