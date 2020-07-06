package com.dcj.meeting.mapper;

import com.dcj.meeting.pojo.entity.view.AppointmentView;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentViewMapper {
    @Select("select * from appointment_view")
    public List<AppointmentView> selectAll();

    @Select("select * from appointment_view where userid=#{userid} limit 1")
    public List<AppointmentView> selectByUserid(String userid);

    @Select("select * from appointment_view where appoint_id=#{appointId} limit 1")
    public AppointmentView selectByAppointId(int appointId);

    @Select("select * from appointment_view where userid=#{userid} and status_code=#{statusCode}")
    public List<AppointmentView> selectOnesHistory(String userid, int statusCode);
}
