package com.dcj.meeting.mapper;

import com.dcj.meeting.pojo.entity.Appointment;
import com.dcj.meeting.pojo.entity.AppointmentStatus;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentMapper {
    @Select("select * from appointment")
    public List<Appointment> selectAll();

    @Select("select * from appointment where appoint_id=#{appointId} limit 1")
    public Appointment selectByAppointId(int appointId);

    @Select("select * from appointment where room_id=#{roomId} and status_code=" + AppointmentStatus.PASSED)
    public List<Appointment> selectPassedHistory(int roomId);

    @Select("select * from appointment where status_code=" + AppointmentStatus.PASSED)
    public List<Appointment> selectAllPassedHistory();

    @Select("select * from appointment where status_code=#{statusCode} order by hand_time asc")
    public List<Appointment> selectByStatusCode(int statusCode);

    @Select("select count(appoint_id) from appointment")
    public int selectTotal();

    @Select("select count(appoint_id) from appointment where hand_time>=#{date}")
    public int selectTodayTotal(Date date);

    @Update("update appointment set userid=#{userid},room_id=#{roomId},purpose=#{purpose},participants=#{participants},phone=#{phone},note=#{note},start_time=#{startTime},duration=#{duration},status_code=#{statusCode},check_info=#{checkInfo} where appoint_id=#{appointId}")
    public int updateAppointment(Appointment appointment);

    @Insert("insert into appointment (userid,room_id,purpose,participants,phone,note,start_time,duration,status_code,check_info,hand_time) values (#{userid},#{roomId},#{purpose},#{participants},#{phone},#{note},#{startTime},#{duration},#{statusCode},#{checkInfo},#{handTime})")
    public int addAppointment(Appointment appointment);

    @Delete("delete from appointment where appoint_id=#{appointId}")
    public int deleteAppointment(Appointment appointment);

    @Delete("delete from appointment where status_code=" + AppointmentStatus.OUT_OF_DATE)
    public int deleteOutOfDate();
}
