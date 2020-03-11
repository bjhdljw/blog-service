package com.ljw.blogservice.service.Impl;

import com.ljw.blogservice.constant.Constant;
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
        if(!StringUtils.isEmpty(blogForAdd.getContent())) {
            setDescription(blog);
            setImage(blog);
        }
        blogDao.insertBlog(blog);
    }

    @Override
    public Blog selectByTitle(String title) {
        Blog blog = blogDao.selectByTitle(title);
        return blog;
    }

    public void setImage(Blog blog) {
        String content = blog.getContent();
        int startOfImage = content.indexOf(Constant.IMAGETAGLEFT) + Constant.IMAGETAGLEFT.length();
        String image = content.substring(startOfImage);
        int endOfImage = image.indexOf("\"");
        image = image.substring(0, endOfImage);
        blog.setImage(image);
    }

    public void setDescription(Blog blog) {
        String content = blog.getContent();
        int startOfDescription = content.indexOf(Constant.PTAGLEFT);
        int endOfDescription = content.indexOf(Constant.PTAGRIGHT);
        String firstParagraph = content.substring(startOfDescription + Constant.PTAGLEFT.length(), endOfDescription);
        String description = filterImageTag(firstParagraph);
        blog.setDescription(description);
    }

    public String filterImageTag(String depscriptionWithImage) {
        int startOfTag = depscriptionWithImage.indexOf(Constant.IMAGETAGLEFT);
        if(startOfTag == -1) {
            return depscriptionWithImage;
        }
        int endOfTag = depscriptionWithImage.indexOf(Constant.TAGRIGHT);
        String halfOfString = depscriptionWithImage.substring(0, startOfTag);
        String anotherHalfOfString = depscriptionWithImage.substring(endOfTag + Constant.TAGRIGHT.length());
        String newString = halfOfString + "<图片>" + anotherHalfOfString;
        newString = filterImageTag(newString);
        return newString;
    }

}
