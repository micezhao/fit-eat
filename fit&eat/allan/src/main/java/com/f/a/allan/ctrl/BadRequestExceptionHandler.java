package com.f.a.allan.ctrl;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class BadRequestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        IybBaseResponse response = new IybBaseResponse();
//        response.setCode(ErrorCodeEnum.CMN_ILLEGAL_ARG.getCode ());
        String message = "";
        BindingResult result = ex.getBindingResult();
        //组装校验错误信息
        if(result.hasErrors()){
            List<ObjectError> list = result.getAllErrors();
            StringBuffer errorMsgBuffer = new StringBuffer();
            for(ObjectError error:list){
                if (error instanceof FieldError) {
                    FieldError errorMessage = (FieldError) error;
                    errorMsgBuffer = errorMsgBuffer.append(errorMessage.getDefaultMessage()+",");
                }
            }
            //返回信息格式处理
            message = errorMsgBuffer.toString().substring(0,errorMsgBuffer.length()-1);
        }
//        response.setMessage(message);
        return new ResponseEntity<Object>(message, headers, status);
    }

}
