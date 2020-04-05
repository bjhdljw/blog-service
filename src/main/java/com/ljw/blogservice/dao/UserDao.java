package com.ljw.blogservice.dao;

import com.ljw.blogservice.domain.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    void insertUser(UserInfo userInfo);

}
