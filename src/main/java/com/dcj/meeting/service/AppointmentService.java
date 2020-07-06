package com.dcj.meeting.service;

import com.dcj.meeting.pojo.entity.Appointment;
import com.dcj.meeting.pojo.entity.view.AppointmentView;

import java.util.List;

public interface AppointmentService {
    public Appointment selectByAppointId(int appointId);

    public AppointmentView selectViewByAppointId(int appointId);

    public List<Appointment> selectByStatusCode(int statusCode);

    public int submitAppointment(Appointment appointment);

    public List<AppointmentView> selectHistoryByStatusCode(String userid, int statusCode);

    public int selectTotal();

    public int selectTodayTotal();

    public int updateAppointment(Appointment appointment);

    //将已过期的记录设置为过期状态
    public void makeOutOfDate();

    //清理删除过期记录
    public int cleanOutOfDate();
}
