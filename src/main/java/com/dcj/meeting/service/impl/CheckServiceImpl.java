package com.dcj.meeting.service.impl;

import com.dcj.meeting.mapper.AppointmentMapper;
import com.dcj.meeting.mapper.CheckViewMapper;
import com.dcj.meeting.pojo.enterprise.AppProperties;
import com.dcj.meeting.pojo.enterprise.message.UserTextMessage;
import com.dcj.meeting.pojo.entity.Appointment;
import com.dcj.meeting.pojo.entity.AppointmentStatus;
import com.dcj.meeting.pojo.entity.Period;
import com.dcj.meeting.pojo.entity.view.CheckView;
import com.dcj.meeting.service.AppointmentService;
import com.dcj.meeting.service.CheckService;
import com.dcj.meeting.service.MessageService;
import com.dcj.meeting.service.RoomService;
import com.dcj.meeting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CheckServiceImpl implements CheckService {
    @Autowired
    AppProperties appProperties;
    @Autowired
    AppointmentMapper appointmentMapper;
    @Autowired
    RoomService roomService;
    @Autowired
    MessageService messageService;
    @Autowired
    CheckViewMapper checkViewMapper;
    @Autowired
    AppointmentService appointmentService;

    @Override
    public List<CheckView> selectChecking() {
        return checkViewMapper.selectAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approveAll(String checker) {
        List<Appointment> appointments = appointmentService.selectByStatusCode(AppointmentStatus.CHECKING);
        for (Appointment appointment : appointments) {
            approve(appointment, checker);
        }
        return appointments.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reject(String checker, List<Appointment> appointments, String checkInfo) {
        for (Appointment appointment : appointments) {
            reject(appointment, checker, checkInfo);
        }
        return appointments.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reject(Appointment appointment, String checker, String checkInfo) {
        appointment.setStatusCode(AppointmentStatus.REJECTED);
        appointment.setCheckInfo(checker + "：" + checkInfo);
        int i = appointmentService.updateAppointment(appointment);
        //发送通知提醒，告知预约请求被退回
        UserTextMessage message = new UserTextMessage();
        message.setAgentid(Integer.valueOf(appProperties.getAgentid()));
        message.setTouser(appointment.getUserid());
        message.setContent(formatted(appointment, "被退回\n审核信息：" + checkInfo));
        messageService.sendUserTextMessage(message);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(Appointment appointment, String checker) {
        int index = 0;
        int roomId = appointment.getRoomId();
        Period period = DateUtil.buildPeriod(appointment.getStartTime(), appointment.getDuration());
        //检查房间时间段是否已被分配
        if (!roomService.isAvailableDuring(roomId, period)) {
            //已被分配给其他人,将预约请求改为失败
            appointment.setStatusCode(AppointmentStatus.REJECTED);
            appointment.setCheckInfo(checker + "：已被他人抢先占用");
            appointmentMapper.updateAppointment(appointment);
            //发送通知提醒，告知已被他人占用
            UserTextMessage message = new UserTextMessage();
            message.setAgentid(Integer.valueOf(appProperties.getAgentid()));
            message.setTouser(appointment.getUserid());
            message.setContent(formatted(appointment, "已被他人抢先占用"));
            messageService.sendUserTextMessage(message);
        } else {
            //未被分配给他人，审核通过
            appointment.setStatusCode(AppointmentStatus.PASSED);
            appointment.setCheckInfo(checker + "：成功通过审核");
            index = appointmentMapper.updateAppointment(appointment);
            if (index > 0) {
                //发送消息通知，告知审核通过
                UserTextMessage message = new UserTextMessage();
                message.setAgentid(Integer.valueOf(appProperties.getAgentid()));
                message.setTouser(appointment.getUserid());
                message.setContent(formatted(appointment, "成功通过审核"));
                messageService.sendUserTextMessage(message);
                //将和通过的预约冲突的预约退回
                List<Appointment> checkings = appointmentMapper.selectByStatusCode(AppointmentStatus.CHECKING);
                for (Appointment checking : checkings) {
                    Period p = DateUtil.buildPeriod(checking.getStartTime(), checking.getDuration());
                    if (!roomService.isAvailableDuring(checking.getRoomId(), p)) {
                        reject(checking, checker, "已被他人抢先占用");
                    }
                }
            }
        }
        return index;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(List<Appointment> appointments, String checker) {
        int count = 0;
        for (Appointment appointment : appointments) {
            if (approve(appointment, checker) > 0) {
                count++;
            }
        }
        return count;
    }

    //预约结果通知格式
    public static String formatted(Appointment appointment, String content) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = format.format(new Date(System.currentTimeMillis()));
        return "会议室预约结果通知\n预约序号：" + appointment.getAppointId() + "\n预约结果：" + content + "\n通知时间：" + date;
    }
}
