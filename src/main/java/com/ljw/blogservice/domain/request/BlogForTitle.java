package com.ljw.blogservice.domain.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class BlogForTitle {

    @NotEmpty
    @Size(max = 255)
    private String title;

}
