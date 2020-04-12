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

    USER_ERROR("02", "用户错误"),
    USER_GENERATE_KEY_ERROR("02001", "生成密钥对错误"),
    USER_NAME_EXIST_ERROR("02002", "用户名已存在"),
    USER_RSA_DESCRYPT_ERROR("02003", "RSA解密异常"),
    USER_MAIL_USED_ERROR("002004", "邮箱已使用"),
    USER_ACTIVE_ERROR("002005", "激活码不能为空"),
    USER_ACTIVE_CODE_ERROR("002006", "激活码错误")
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
