package com.ljw.blogservice.domain.request;

import lombok.Data;

@Data
public class Login {

    private String userName;

    /**
     * 使用对称密钥加密过的密码
     */
    private String password;

}
