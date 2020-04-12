package com.ljw.blogservice.domain.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SetAESKey {

    @NotEmpty
    @Size(max = 50)
    private String userName;

    /**
     * 使用公钥加密之后的AES密钥
     */
    @NotEmpty
    private String key;

}
