package com.ljw.blogservice.controller;

import com.ljw.blogservice.domain.Blog;
import com.ljw.blogservice.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @ResponseBody
    @RequestMapping("/listBlogs")
    public List<Blog> listBlogs() {
        List<Blog> blogs = blogService.listAllBlogs();
        return blogs;
    }

}
