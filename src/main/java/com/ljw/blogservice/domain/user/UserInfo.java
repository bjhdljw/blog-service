package com.ljw.blogservice.domain.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class UserInfo {

    private String uuid;

    @NotEmpty
    @Size(max = 50)
    private String userName;

    @NotEmpty
    @Size(max = 255)
    private String mail;

    /**
     * 使用AES算法加密之后的密码
     */
    @NotEmpty
    private String password;

    private String phone;

    private String wechatId;

    private String roleId;

    private Integer status;

    private Date createTime;

    private Date lastLoginTime;

}
