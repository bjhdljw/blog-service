package com.ljw.blogservice.domain.response;

public class Result {

    private Integer code = 1;

    private String message = "成功";

    private Object Data;

    public Result setData(Object data) {
        Data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return Data;
    }
}
