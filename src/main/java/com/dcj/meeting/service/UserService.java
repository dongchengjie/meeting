package com.dcj.meeting.service;

import com.dcj.meeting.pojo.enterprise.UserInfo;
import com.dcj.meeting.pojo.entity.User;

public interface UserService {
    //是否存在用户
    public boolean ifExistsUser(String userid);

    public int createUser(String userid, UserInfo userInfo);

    //用户总数
    public int selectTotal();

    public User selectById(int id);
}
