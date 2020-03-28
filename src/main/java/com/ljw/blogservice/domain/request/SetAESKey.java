package com.ljw.blogservice.domain.request;

import lombok.Data;

@Data
public class SetAESKey {

    private String userName;

    private String key;

}
