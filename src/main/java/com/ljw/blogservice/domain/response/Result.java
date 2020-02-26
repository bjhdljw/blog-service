package com.ljw.blogservice.domain.response;

import lombok.Data;

@Data
public class Result {

    private Integer code = 1;

    private String message = "成功";

    private Object Data;

}
