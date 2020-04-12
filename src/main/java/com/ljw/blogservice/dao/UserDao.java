package com.ljw.blogservice.dao;

import com.ljw.blogservice.domain.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    void insertUser(UserInfo userInfo);

    List<UserInfo> getListByUserName(String userName);

    List<UserInfo> getListByMail(String mail);

}
