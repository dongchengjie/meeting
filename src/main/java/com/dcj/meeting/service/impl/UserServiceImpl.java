package com.dcj.meeting.service.impl;

import com.dcj.meeting.mapper.ProfileMapper;
import com.dcj.meeting.mapper.UserMapper;
import com.dcj.meeting.pojo.enterprise.UserInfo;
import com.dcj.meeting.pojo.entity.Profile;
import com.dcj.meeting.pojo.entity.User;
import com.dcj.meeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ProfileMapper profileMapper;

    @Override
    public boolean ifExistsUser(String userid) {
        return userMapper.selectByUserid(userid) != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createUser(String userid, UserInfo userInfo) {
        //添加用户
        User user = new User();
        user.setUserid(userid);
        user.setCreateTime(new Date());
        userMapper.addUser(user);
        User newUser = userMapper.selectByUserid(userid);
        //同步用户资料
        Profile profile = new Profile();
        profile.setId(newUser.getId());
        profile.setUserid(userid);
        profile.setAvatar(userInfo.getAvatar());
        profile.setEmail(userInfo.getEmail());
        profile.setGender(userInfo.getGender());
        profile.setName(userInfo.getName());
        profile.setPhone(userInfo.getMobile());
        profile.setRole(decideRole(userInfo));//身份判定方法可改
        return profileMapper.addProfile(profile);
    }

    @Override
    public int selectTotal() {
        return userMapper.selectTotal();
    }

    @Override
    public User selectById(int id) {
        return userMapper.selectById(id);
    }

    //判断身份方法
    private int decideRole(UserInfo userInfo) {
        //判断方法（可根据情况改动）：长度为10的userid为学生，否则为老师
        return userInfo.getUserid().length() == 10 ? 1 : 2;
    }
}
