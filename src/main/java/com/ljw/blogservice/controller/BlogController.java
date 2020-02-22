package com.ljw.blogservice.controller;

import com.ljw.blogservice.domain.Blog;
import com.ljw.blogservice.domain.request.BlogForAdd;
import com.ljw.blogservice.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/blogController")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @ResponseBody
    @RequestMapping("/listBlogs")
    public List<Blog> listBlogs() {
        List<Blog> blogs = blogService.listAllBlogs();
        return blogs;
    }

    @ResponseBody
    @RequestMapping(value = "/addBlog", method = RequestMethod.POST)
    public void insertBlog(@RequestBody @Valid BlogForAdd blogForAdd, BindingResult bindingResult) {
        blogService.insertBlog(blogForAdd);
    }

}
