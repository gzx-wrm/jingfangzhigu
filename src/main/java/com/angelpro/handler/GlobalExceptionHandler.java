package com.angelpro.handler;

import com.angelpro.exception.ChatException;
import com.angelpro.exception.FeedbackException;
import com.angelpro.exception.LoginException;
import com.angelpro.exception.RegisterException;
import com.angelpro.vo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description 处理全局异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 加上该注解表明这是一个异常处理方法，注解的值为要拦截的异常类
    @ExceptionHandler(value = {LoginException.class, RegisterException.class, FeedbackException.class})
    public Result handleLoginException(Exception e) {
        if (e != null)
            return Result.error(e.getMessage());
        else
            return Result.error();
    }

    @ExceptionHandler(value = {ChatException.class})
    public Result handleChatException(Exception e) {
        if (e != null)
            return Result.error(e.getMessage());
        else
            return Result.error();
    }

}
