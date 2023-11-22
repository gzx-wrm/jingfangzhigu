package com.example.handler;

import com.example.exception.ChatException;
import com.example.exception.FeedbackException;
import com.example.exception.LoginException;
import com.example.exception.RegisterException;
import com.example.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description 处理全局异常
 */
@ControllerAdvice
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
