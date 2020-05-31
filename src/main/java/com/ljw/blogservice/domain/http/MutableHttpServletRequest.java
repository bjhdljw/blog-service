package com.ljw.blogservice.domain.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;

    public MutableHttpServletRequest(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
        this.customHeaders = new HashMap<>();
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    public String getHeader(String name) {
        String value = customHeaders.get(name);
        if(null != value) {
            return value;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

}
