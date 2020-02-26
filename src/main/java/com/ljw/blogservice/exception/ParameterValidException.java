package com.ljw.blogservice.exception;

import lombok.Data;

public class ParameterValidException extends BlogException{

    private String code;

    public ParameterValidException(String message) {
        super(message);
    }

    public ParameterValidException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
