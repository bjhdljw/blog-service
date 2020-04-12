package com.ljw.blogservice.domain.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PublicKeyGet {

    @NotEmpty
    @Size(max = 50)
    private String userName;

}
