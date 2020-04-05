package com.ljw.blogservice.domain.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {

    private String uuid;

    private String userName;

    private String mail;

    /**
     * 使用AES算法加密之后的密码
     */
    private String password;

    private String phone;

    private String wechatId;

    private String roleId;

    private Integer status;

    private Date createTime;

    private Date lastLoginTime;

}
