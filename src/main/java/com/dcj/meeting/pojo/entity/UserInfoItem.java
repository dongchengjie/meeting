package com.dcj.meeting.pojo.entity;

import java.util.Date;

/**
 * @author 董成杰
 * @date 2020/3/17 20:49
 */
public class UserInfoItem extends Profile {
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static UserInfoItem build(Profile profile, User user) {
        UserInfoItem item = new UserInfoItem();
        item.setId(profile.getId());
        item.setRole(profile.getRole());
        item.setUserid(profile.getUserid());
        item.setAvatar(profile.getAvatar());
        item.setName(profile.getName());
        item.setAge(profile.getAge());
        item.setGender(profile.getGender());
        item.setPhone(profile.getPhone());
        item.setEmail(profile.getEmail());
        item.setCreateTime(user.getCreateTime());
        return item;
    }
}
