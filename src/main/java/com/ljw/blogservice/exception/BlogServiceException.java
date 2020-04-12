package com.ljw.blogservice.exception;

public class BlogServiceException extends BlogException {

    private String code;

    public BlogServiceException(String message) {
        super(message);
    }

    public BlogServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
