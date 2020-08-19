package com.fa.kater.ctrl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * restful 接口统一的异常处理类
 * @author micezhao
 *
 */
@ControllerAdvice
@RestController
public class RestfulApiExceptionHandler {
	
	// 这里处理没有被上一个方法处理的异常，通常在最后的方法处理最大的Exception，保证兜底
    @ExceptionHandler(value = Exception.class)
    public Map<String, Object> exceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        Map<String, Object> error = new HashMap<String, Object>();
        error.put("status", 500);
        error.put("message", exception.getMessage());
        return error;
    }
}
