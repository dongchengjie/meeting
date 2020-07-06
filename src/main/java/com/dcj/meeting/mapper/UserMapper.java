package com.dcj.meeting.mapper;

import com.dcj.meeting.pojo.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    @Select("select * from user")
    public List<User> selectAll();

    @Select("select * from user where id=#{id} limit 1")
    public User selectById(int id);

    @Select("select count(id) from user")
    public int selectTotal();

    @Select("select * from user where userid=#{userid} limit 1")
    public User selectByUserid(String userid);

    @Update("update user set userid=#{userid},create_time=#{createTime} where id=#{id}")
    public int updateUser(User user);

    @Insert("insert into user (userid,create_time) values (#{userid},#{createTime})")
    public int addUser(User user);

    @Delete("delete from user where id=#{id}")
    public int deleteUser(User user);
}
