package com.ljw.blogservice.service;

import com.ljw.blogservice.domain.Blog;
import com.ljw.blogservice.domain.request.BlogForAdd;

import java.util.List;

public interface BlogService {

    List<Blog> listAllBlogs();

    void insertBlog(BlogForAdd blogForAdd);

    Blog selectByTitle(String title);

}
