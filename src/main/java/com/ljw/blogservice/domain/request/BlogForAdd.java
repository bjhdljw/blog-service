package com.ljw.blogservice.domain.request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class BlogForAdd {

    @NotEmpty
    @Size(max = 255)
    private String title;

    @NotEmpty
    @Size(max = 10000)
    private String content;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer classId;

}
