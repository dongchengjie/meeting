package com.dcj.meeting.mapper;

import com.dcj.meeting.pojo.entity.Profile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileMapper {
    @Select("select * from profile")
    public List<Profile> selectAll();

    @Select("select * from profile where id=#{id} limit 1")
    public Profile selectById(int id);

    @Select("select * from profile where userid=#{userid} limit 1")
    public Profile selectByUserid(String userid);

    @Update("update profile set role=#{role},userid=#{userid},avatar=#{avatar},name=#{name},age=#{age},gender=#{gender},phone=#{phone},email=#{email} where id=#{id}")
    public int updateProfile(Profile profile);

    @Insert("insert into profile (id,role,userid,avatar,name,age,gender,phone,email) values (#{id},#{role},#{userid},#{avatar},#{name},#{age},#{gender},#{phone},#{email})")
    public int addProfile(Profile profile);

    @Delete("delete from profile where id=#{id}")
    public int deleteProfile(Profile profile);
}
