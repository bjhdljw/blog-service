package com.ljw.blogservice.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Component
public class BlogExceptionHandler {

    @ResponseBody
    @ExceptionHandler
    public Map parameterValidExceptionHandler(ParameterValidException parameterValidException) {
        //key - value
        //索引：哈希表；树，O(1)
        //ArrayList O(n)
        Map<String, Object> map = new HashMap<>();
        map.put("code", parameterValidException.getCode());
        map.put("message", parameterValidException.getMessage());
        return map;
    }

}
