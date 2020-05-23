package com.ljw.blogservice.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class RoleCheckAspect {

    @Pointcut(value = "execution(* com.ljw.blogservice.controller..*.*(..))")
    public void aspect() {

    }

    @Before("aspect()")
    public void doBefore(JoinPoint joinPoint) {
        //todo 校验用户权限
    }

}
