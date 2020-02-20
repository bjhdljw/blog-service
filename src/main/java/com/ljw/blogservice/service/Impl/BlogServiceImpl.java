package com.ljw.blogservice.service.Impl;

import com.ljw.blogservice.dao.BlogDao;
import com.ljw.blogservice.domain.Blog;
import com.ljw.blogservice.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Override
    public List<Blog> listAllBlogs() {
        List<Blog> blogs = blogDao.listAllBlogs();
        return blogs;
    }
}
