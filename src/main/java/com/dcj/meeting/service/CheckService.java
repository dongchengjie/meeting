package com.dcj.meeting.service;

import com.dcj.meeting.pojo.entity.Appointment;
import com.dcj.meeting.pojo.entity.view.CheckView;

import java.util.List;

public interface CheckService {
    public List<CheckView> selectChecking();

    public int approveAll(String checker);

    public int approve(Appointment appointment, String checker);

    public int approve(List<Appointment> appointments,String checker);

    public int reject(Appointment appointment, String checker, String checkInfo);

    public int reject(String checker, List<Appointment> appointments, String checkInfo);
}
