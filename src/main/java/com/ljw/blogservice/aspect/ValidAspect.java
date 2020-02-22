package com.ljw.blogservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Aspect
@Configuration
public class ValidAspect {

    @Pointcut(value = "execution(* com.ljw.blogservice.controller..*.*(..))")
    public void aspect() {

    }

    @Before("aspect()")
    public void doBefore(JoinPoint joinPoint) throws Exception{
        Object[] argcs = joinPoint.getArgs();
        List<String> list = new ArrayList<>();
        for(Object obj : argcs) {
            if(obj instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult)obj;
                if(bindingResult.hasErrors()) {
                    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
                    for(FieldError fieldError : fieldErrors) {
                        list.add(fieldError.getField());
                    }
                    throw new Exception("参数：" + list.toString() + "不合法");
                }
            }
        }
    }

}
