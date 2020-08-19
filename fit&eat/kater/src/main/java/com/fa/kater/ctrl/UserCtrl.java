package com.fa.kater.ctrl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.contants.Contants;
import com.f.a.kobe.view.UserAgent;
import com.fa.kater.biz.UserInfoBiz;
import com.fa.kater.entity.requset.UserInfoRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("user")
@Slf4j
public class UserCtrl {


    @Autowired
    private UserInfoBiz userBiz;
    
    /**
     * TODO 以后迁移到消息中心
     * 获取手机号验证码
     * @param userAgent
     * @return
     */
    @GetMapping("smscodeAttain")
    public ResponseEntity<Object> smscodeAttain(UserAgent userAgent){
    	log.debug("请求获取短信验证码");
    	String smscode =String.valueOf((int)((Math.random()*9+1)*100000));
    	Map<String,String> result = new HashMap<String,String>();
    	result.put("validateCode", smscode);
    	return new ResponseEntity<Object>(result,HttpStatus.OK); 
    }
    
    /**
     * TODO 以后迁移到消息中心
     * 验证码校验
     * @param userAgent
     * @param code
     * @return
     */
    @GetMapping("smscodeVaildate/{code}")
    public ResponseEntity<Object> smscodeVaildate(UserAgent userAgent,@PathVariable("code") String code){
    	log.debug("请求验证短信验证码");
    	Map<String,Boolean> result = new HashMap<String,Boolean>();
    	result.put("validateResult", true);
    	return new ResponseEntity<Object>(result,HttpStatus.OK); 
    }
    
    @PutMapping("mobileBind/{mobile}")
    public ResponseEntity<Boolean> mobileBind(UserAgent userAgent,@PathVariable("mobile") String mobile,HttpSession session){
    	log.info("准备进行绑定手机号");
    	UserAgent updatedUserAgent= userBiz.bindAccountByMobile(mobile, userAgent);
    	session.setAttribute(Contants.USER_AGENT, updatedUserAgent);
    	return new ResponseEntity<Boolean>(true,HttpStatus.OK); 
    }
    
    
    @PostMapping("/fillup")
    public ResponseEntity<Object> userInfoFillup(HttpSession session,UserAgent userAgent,@RequestBody UserInfoRequest request){
    	userBiz.fillUserInfo(request, userAgent);
    	
    	session.setAttribute(Contants.USER_AGENT, userAgent);
    	return null;
    }
    
}
