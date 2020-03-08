package com.ljw.blogservice.service.Impl;

import com.ljw.blogservice.dao.BlogDao;
import com.ljw.blogservice.domain.Blog;
import com.ljw.blogservice.domain.request.BlogForAdd;
import com.ljw.blogservice.service.BlogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        //获取博客摘要
        if(StringUtils.isEmpty(blogForAdd.getContent())) {
            String content = blogForAdd.getContent();
            int endOfDescription = content.indexOf("</p>");
            blog.setDescription(content.substring(3, endOfDescription));
            //获取第一张图片的标签
            int startOfImage = content.indexOf("img src=\"") + 9;
            String image = content.substring(startOfImage);
            int endOfImage = image.indexOf("\"");
            image = image.substring(0, endOfImage);
            blog.setImage(image);
        }
        blogDao.insertBlog(blog);
    }

    @Override
    public Blog selectByTitle(String title) {
        Blog blog = blogDao.selectByTitle(title);
        return blog;
    }
}
