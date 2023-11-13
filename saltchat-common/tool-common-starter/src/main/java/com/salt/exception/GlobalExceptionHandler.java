package com.salt.exception;

import com.salt.enums.ResponseEnum;
import com.salt.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author 徐志斌
 * @Date: 2023/6/30 21:22
 * @Version 1.0
 * @Description: 全局统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 自定义异常 SaltException
     */
    @ExceptionHandler(SaltException.class)
    public R bingoException(SaltException e) {
        return R.out(e.getResponseEnum());
    }

    /**
     * 所有异常
     */
    @ExceptionHandler(Exception.class)
    public R bindException(Exception e) {
        log.error("========================Exception：{}========================", e);
        return R.out(ResponseEnum.FAIL, e);
    }
}
