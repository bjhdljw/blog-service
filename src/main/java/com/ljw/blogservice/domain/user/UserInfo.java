package com.ljw.blogservice.domain.user;

import lombok.Data;

@Data
public class UserInfo {

    private String userName;

    private String mail;

    /**
     * 使用AES算法加密之后的密码
     */
    private String passWord;

}
