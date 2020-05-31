package com.ljw.blogservice.aspect;

import com.ljw.blogservice.annotation.RequireRole;
import com.ljw.blogservice.constant.ResponseCode;
import com.ljw.blogservice.exception.BlogServiceException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Configuration
public class RoleCheckAspect {

    @Pointcut(value = "execution(* com.ljw.blogservice.controller..*.*(..))")
    public void aspect() {

    }

    @Before("aspect()")
    public void doBefore(JoinPoint joinPoint) {
        //1.拿到这个接口需要的角色（@RequireRole()）
        //2.检查用户是否登陆，如果登陆了拿到用户信息 -> 使用filter来实现
        //3.根据用户信息拿到登陆的用户的角色
        //3.用户的角色是否被包含于要求的角色
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if(null == method.getAnnotation(RequireRole.class)) {
            return;
        }
        String[] roles = method.getAnnotation(RequireRole.class).roles();
        Object[] args = joinPoint.getArgs();
        if(null == args || 0 == args.length) {
            throw new BlogServiceException(ResponseCode.USER_AUTH_CHECK_ERROR.getCode(), ResponseCode.USER_AUTH_CHECK_ERROR.getMessage());
        }
        for(Object arg : args) {
            if(arg instanceof HttpServletRequest) {
                String userUuid = ((HttpServletRequest) arg).getHeader("loginUuid");
                //todo 根据userUuid从缓存里面拿用户角色
                String userRole = null;
                if(!Arrays.asList(roles).contains(userRole)) {
                    throw new BlogServiceException(ResponseCode.USER_AUTH_ERROR.getCode(), ResponseCode.USER_AUTH_ERROR.getMessage());
                }
            }
        }
    }

}
