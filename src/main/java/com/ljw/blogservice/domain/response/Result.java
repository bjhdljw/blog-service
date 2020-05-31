package com.ljw.blogservice.domain.response;

public class Result {

    private String code = "1";

    private String message = "成功";

    private Object Data;

    public Result setData(Object data) {
        Data = data;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
