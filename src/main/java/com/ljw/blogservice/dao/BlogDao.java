package com.ljw.blogservice.dao;

import com.ljw.blogservice.domain.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogDao {

    List<Blog> listAllBlogs();

    void insertBlog(Blog blog);

}
