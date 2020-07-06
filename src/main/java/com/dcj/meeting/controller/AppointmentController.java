package com.dcj.meeting.controller;

import com.dcj.meeting.pojo.enterprise.UserIdentity;
import com.dcj.meeting.pojo.entity.Appointment;
import com.dcj.meeting.pojo.entity.AppointmentStatus;
import com.dcj.meeting.pojo.entity.Room;
import com.dcj.meeting.pojo.feedback.Error;
import com.dcj.meeting.pojo.feedback.Feedback;
import com.dcj.meeting.pojo.server.OpenProperties;
import com.dcj.meeting.service.AppointmentService;
import com.dcj.meeting.service.RoomService;
import com.dcj.meeting.util.AuthUtil;
import com.dcj.meeting.util.BehaviorUtil;
import com.dcj.meeting.util.CheckUtil;
import com.dcj.meeting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @Autowired
    RoomService roomService;

    @Autowired
    OpenProperties openProperties;

    @GetMapping("/appointment/{roomId}")
    public String appointmentView(@PathVariable("roomId") int roomId, Model model) {
        Room room = roomService.selectRoomById(roomId);
        model.addAttribute("room", room);
        return "appointment";
    }

    @PostMapping("/appointment")
    @ResponseBody
    public Object appoint(Integer roomId, Long datetime,
                          Integer duration, Integer size, String phone,
                          String purpose, String note, HttpSession session) {
        //防止频繁提交
        if (BehaviorUtil.isFrequentRequest(session)) {
            return new Feedback(4003, "操作频繁");
        }
        UserIdentity userIdentity = AuthUtil.takeUserIdentity(session);
        if (userIdentity == null) {
            return Error.invaildAccess();
        }
        Appointment appointment = new Appointment();
        //装填数据，并设置为等待审核
        appointment.setChecking(userIdentity.getUserId(), roomId, datetime, duration, size, phone, purpose, note);
        //检测预约时间段内，会议室是否开放
        if (!DateUtil.opening(openProperties.getStartHour(), openProperties.getCloseHour(), appointment.getStartTime(), appointment.getRoomId())) {
            return new Feedback(3003, "预约的时间段为会议室非开放时间段");
        }
        //检查数据格式
        if (!CheckUtil.isAppointmentInvalid(appointment)) {
            return new Error(4002, "数据格式有误(联系电话或占用时长)");
        }
        if (appointmentService.submitAppointment(appointment) > 0) {
            return new Feedback(0, "ok");
        } else {
            return new Feedback(2003, "提交预约失败");
        }
    }

    @GetMapping("/appointment/cancel/{appointId}")
    public Object cancelAppointment(@PathVariable("appointId") Integer appointId, Model model) {
        Appointment appointment = appointmentService.selectByAppointId(appointId);
        appointment.setStatusCode(AppointmentStatus.CANCELED);
        appointment.setCheckInfo("预约已取消");
        if (appointmentService.updateAppointment(appointment) > 0) {
            return "redirect:/history/detail/" + appointId;
        }
        return Error.build(2006, "取消预约失败", "/meeting/index", "返回首页", model);
    }
}
