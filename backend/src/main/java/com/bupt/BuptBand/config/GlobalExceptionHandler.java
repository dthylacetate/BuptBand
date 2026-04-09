package com.bupt.BuptBand.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    /**
     * 捕获我们在代码里手动 throw 的所有 RuntimeException
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e)
    {
        // 打印到后台控制台方便我们查错
        System.err.println("拦截到业务异常: " + e.getMessage());

        // 返回 400 状态码，并直接把中文报错信息发给前端
        // 这样前端只需直接展示 response.data 就能弹出友好的提示框
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * 以后如果你有其他特定异常（比如数据库挂了），也可以在这里加对应的方法
     */
}