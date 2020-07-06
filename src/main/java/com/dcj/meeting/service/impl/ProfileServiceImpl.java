package com.dcj.meeting.service.impl;

import com.dcj.meeting.mapper.ProfileMapper;
import com.dcj.meeting.pojo.entity.Profile;
import com.dcj.meeting.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    ProfileMapper profileMapper;

    @Override
    public Profile selectProfileByUserid(String userid) {
        return profileMapper.selectByUserid(userid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProfile(Profile profile) {
        return profileMapper.updateProfile(profile);
    }

    @Override
    public List<Profile> selectAll() {
        return profileMapper.selectAll();
    }


}
