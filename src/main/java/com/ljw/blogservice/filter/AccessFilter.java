package com.ljw.blogservice.filter;

import com.alibaba.druid.support.json.JSONUtils;
import com.ljw.blogservice.constant.RedisConstant;
import com.ljw.blogservice.constant.ResponseCode;
import com.ljw.blogservice.domain.http.MutableHttpServletRequest;
import com.ljw.blogservice.domain.response.Result;
import com.ljw.blogservice.exception.BlogServiceException;
import com.ljw.blogservice.util.JWTUtil;
import com.ljw.blogservice.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@WebFilter
public class AccessFilter implements Filter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        //0.拿到token，没有token的话，就直接抛异常
        //1.解析token，校验randomCode（看缓存里面有没有这个key）
        //2.根据randomCode，拿到缓存中的用户信息
        //3.将用户信息存入请求头header（实现方式：封装HttpServletRequest）
        //4.手动按照格式规范返回错误信息给前端
        try {
            String token = ((HttpServletRequest)servletRequest).getHeader("token");
            if(StringUtils.isEmpty(token)) {
                throw new BlogServiceException(ResponseCode.USER_LOGIN_TOKEN_ERROR.getCode(), ResponseCode.USER_LOGIN_TOKEN_ERROR.getMessage());
            }
            Map<String, Object> map = JWTUtil.decode(token, RequestUtil.getIpAddr((HttpServletRequest)servletRequest));
            String randomCode = (String) map.get("randomCode");
            if(null == randomCode || !redisTemplate.hasKey(RedisConstant.TOKEN + randomCode)) {
                throw new BlogServiceException(ResponseCode.USER_LOGIN_TOKEN_ERROR.getCode(), ResponseCode.USER_LOGIN_TOKEN_ERROR.getMessage());
            }
            String userUuid = (String) map.get("userUuid");
            MutableHttpServletRequest mutableHttpServletRequest = new MutableHttpServletRequest((HttpServletRequest)servletRequest);
            mutableHttpServletRequest.putHeader("loginUuid", userUuid);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (BlogServiceException e) {
            Result result = new Result();
            result.setCode(e.getCode());
            result.setMessage(e.getMessage());
            servletResponse.getWriter().print(JSONUtils.toJSONString(result));
        }

    }

}
