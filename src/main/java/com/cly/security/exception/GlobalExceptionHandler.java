package com.cly.security.exception;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cly.security.common.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{

    //处理所有未捕获的异常
    @ExceptionHandler
    public R handleException(Exception e){
        return R.fail(500,e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public R handleBusinessException(RuntimeException e) {
        return R.fail(StringUtils.isBlank(e.getMessage())?"系统异常":"系统异常："+e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public R handleNullPointerException(NullPointerException e){
        return R.fail("空指针异常");
    }



}
