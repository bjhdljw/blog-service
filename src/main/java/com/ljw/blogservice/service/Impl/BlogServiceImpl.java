package com.ljw.blogservice.service.Impl;

import com.ljw.blogservice.dao.BlogDao;
import com.ljw.blogservice.domain.Blog;
import com.ljw.blogservice.domain.request.BlogForAdd;
import com.ljw.blogservice.service.BlogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Override
    public List<Blog> listAllBlogs() {
        List<Blog> blogs = blogDao.listAllBlogs();
        return blogs;
    }

    @Override
    public void insertBlog(BlogForAdd blogForAdd) {
        //TODO
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogForAdd, blog);
        blog.setUuid(uuid);
        int end = blog.getContent().length() - 1;
        //三目运算符
        String description = blog.getContent().substring(0, end > 100 ? 100 : end);
        blog.setDescription(description);
        blogDao.insertBlog(blog);
    }

}
