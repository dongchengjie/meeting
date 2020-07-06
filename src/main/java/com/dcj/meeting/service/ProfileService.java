package com.dcj.meeting.service;

import com.dcj.meeting.pojo.entity.Profile;

import java.util.List;

public interface ProfileService {
    public Profile selectProfileByUserid(String userid);

    public int updateProfile(Profile profile);

    public List<Profile> selectAll();
}
