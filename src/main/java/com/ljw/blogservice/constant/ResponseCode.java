package com.ljw.blogservice.constant;

public enum ResponseCode {

    /**
     * 错误码实例：
     * 01-博客服务
     * 001-新建博客错误
     */
    BLOG_ERROR("01", "博客错误"),
    BLOG_ADD_PARAMTER_ERROR("01001", "新建博客参数错误"),
    BLOG_TITLE_EXISTED_ERROR("01002", "新建博客标题已存在"),
    ;

    private final String code;

    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
