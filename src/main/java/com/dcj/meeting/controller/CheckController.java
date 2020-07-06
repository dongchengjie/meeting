package com.dcj.meeting.controller;

import com.dcj.meeting.pojo.entity.Admin;
import com.dcj.meeting.pojo.entity.Appointment;
import com.dcj.meeting.pojo.entity.LayuiTable;
import com.dcj.meeting.pojo.entity.view.CheckView;
import com.dcj.meeting.pojo.feedback.Error;
import com.dcj.meeting.pojo.feedback.Feedback;
import com.dcj.meeting.pojo.server.AutoCheckProperties;
import com.dcj.meeting.service.AppointmentService;
import com.dcj.meeting.service.CheckService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 董成杰
 * @date 2020/3/17 22:03
 */
@Controller
public class CheckController {
    @Autowired
    CheckService checkService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    AutoCheckProperties autoCheckProperties;

    @GetMapping("/check/all")
    @ResponseBody
    public Object getAdmins(Integer page, Integer size) {
        LayuiTable<CheckView> checkTable = new LayuiTable<>();
        //分页
        Page<Object> objectPage = PageHelper.startPage(page, size);
        checkTable.getData().addAll(checkService.selectChecking());
        checkTable.setCount(objectPage.getTotal());
        return checkTable;
    }

    @PostMapping("/check/reject")
    @ResponseBody
    public Object reject(Integer[] ais, String checkInfo, HttpSession session) {
        if (autoCheckProperties.isEnabled()) {
            return new Feedback(1009, "自动审核运行中，人工审核停用");
        }
        Admin admin = (Admin) session.getAttribute("admin");
        List<Appointment> appointmentList = new ArrayList<>();
        if (ais == null) {
            return new Feedback(2007, "预约号参数为空或不合法");
        }
        for (int appointmentId : ais) {
            appointmentList.add(appointmentService.selectByAppointId(appointmentId));
        }
        if (checkService.reject(admin.getUsername(), appointmentList, admin.getUsername() + "：" + checkInfo) > 0) {
            return new Feedback(0, "退回成功");
        } else {
            return new Error(2004, "退回预约失败");
        }
    }

    @PostMapping("/check/approve")
    @ResponseBody
    public Object approve(Integer[] ais, HttpSession session) {
        if (autoCheckProperties.isEnabled()) {
            return new Feedback(1009, "自动审核运行中，人工审核停用");
        }
        Admin admin = (Admin) session.getAttribute("admin");
        List<Appointment> appointmentList = new ArrayList<>();
        if (ais == null) {
            return new Feedback(2007, "预约号参数为空或不合法");
        }
        for (int appointmentId : ais) {
            appointmentList.add(appointmentService.selectByAppointId(appointmentId));
        }
        if (checkService.approve(appointmentList, admin.getUsername()) > 0) {
            return new Feedback(0, "通过预约成功");
        } else {
            return new Error(2005, "通过预约失败");
        }
    }


}
