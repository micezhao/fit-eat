//package com.fa.kater.ctrl;
//
//import com.f.a.kobe.contants.Contants;
//import com.f.a.kobe.view.UserAgent;
//import com.fa.kater.biz.UserBiz;
//import com.fa.kater.customer.pojo.UserInfo;
//import com.fa.kater.exceptions.ErrRtn;
//import com.fa.kater.exceptions.InvaildException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpSession;
//
//@RestController
//@RequestMapping("user")
//public class UserCtrl {
//
//
//    @Autowired
//    private UserBiz userBiz;
//
//    //修改个人信息
//    @PutMapping("/userinfo")
//    public ResponseEntity<Object> updateUserInfo(@RequestBody UserInfo userInfo, UserAgent userAgent, HttpSession httpSession){
//        try{
//            UserAgent agent = userBiz.updateUserInfo(userInfo, userAgent);
//            httpSession.setAttribute(Contants.USER_AGENT,agent);
//        }catch(InvaildException ex){
//            return new ResponseEntity<Object>(
//                    new ErrRtn(ex.getErrCode(), ex.getErrMsg()),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return new ResponseEntity<Object>(HttpStatus.OK);
//    }
//}
