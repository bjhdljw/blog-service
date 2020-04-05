package com.ljw.blogservice.service.user;

import com.ljw.blogservice.domain.request.SetAESKey;
import com.ljw.blogservice.domain.user.UserInfo;

public interface UserService {

    String createPublicKey(String userName) throws Exception;

    void setKey(SetAESKey setAESKey) throws  Exception;

    void register(UserInfo userInfo) throws Exception;

    void active(String code) throws Exception;

    void addUser(UserInfo userInfo);

}
