package com.ljw.blogservice.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Blog {

    private String uuid;

    private String title;

    private String image;

    private String description;

    //驼峰式命名
    private Date createTime;

    private Date updateTime;

    private String content;

    private Integer commentCount;

    private Integer likeCount;

    private Integer collectCount;

    private Integer browseCount;

    private Integer isRecommend;

    private Integer isHot;

    private Integer status;

    private Integer classId;

    private String cateName;

}
