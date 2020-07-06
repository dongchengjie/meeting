package com.dcj.meeting.service.impl;

import com.dcj.meeting.mapper.AppointmentMapper;
import com.dcj.meeting.mapper.AppointmentViewMapper;
import com.dcj.meeting.pojo.entity.Appointment;
import com.dcj.meeting.pojo.entity.AppointmentStatus;
import com.dcj.meeting.pojo.entity.view.AppointmentView;
import com.dcj.meeting.service.AppointmentService;
import com.dcj.meeting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    AppointmentMapper appointmentMapper;
    @Autowired
    AppointmentViewMapper appointmentViewMapper;

    @Override
    public Appointment selectByAppointId(int appointId) {
        return appointmentMapper.selectByAppointId(appointId);
    }

    @Override
    public AppointmentView selectViewByAppointId(int appointId) {
        return appointmentViewMapper.selectByAppointId(appointId);
    }

    @Override
    public List<Appointment> selectByStatusCode(int appointId) {
        return appointmentMapper.selectByStatusCode(AppointmentStatus.CHECKING);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitAppointment(Appointment appointment) {
        return appointmentMapper.addAppointment(appointment);
    }

    @Override
    public List<AppointmentView> selectHistoryByStatusCode(String userid, int statusCode) {
        return appointmentViewMapper.selectOnesHistory(userid, statusCode);
    }

    @Override
    public int selectTotal() {
        return appointmentMapper.selectTotal();
    }

    @Override
    public int selectTodayTotal() {
        //获取今日凌晨0点日期
        Date todayStart = DateUtil.getTodayStart();
        return appointmentMapper.selectTodayTotal(todayStart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAppointment(Appointment appointment) {
        return appointmentMapper.updateAppointment(appointment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeOutOfDate() {
        List<Appointment> appointments = appointmentMapper.selectAll();
        for (Appointment appointment : appointments) {
            if (appointment.getStatusCode() != AppointmentStatus.CHECKING) {
                if (DateUtil.ifOutOfDate(appointment.getStartTime(), appointment.getDuration())) {
                    appointment.setStatusCode(AppointmentStatus.OUT_OF_DATE);
                    appointment.setCheckInfo("预约已过期");
                    appointmentMapper.updateAppointment(appointment);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cleanOutOfDate() {
        return appointmentMapper.deleteOutOfDate();
    }
}
