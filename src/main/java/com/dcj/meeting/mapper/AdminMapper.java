package com.dcj.meeting.mapper;

import com.dcj.meeting.pojo.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    @Select("select * from admin where username=#{username}")
    public Admin selectAdminByUsername(String username);

    @Select("select * from admin where id=#{id}")
    public Admin selectAdminById(int id);

    @Select("select * from admin")
    public List<Admin> selectAll();

    @Update("update admin set password=#{password},privileged=#{privileged} where username=#{username}")
    public int updateAdmin(Admin admin);

    @Insert("insert into admin (username,password,privileged,create_time) values (#{username},#{password},#{privileged},#{createTime})")
    public int addAdmin(Admin admin);

    @Delete("delete from admin where id=#{id}")
    public int deleteAdmin(int id);
}
