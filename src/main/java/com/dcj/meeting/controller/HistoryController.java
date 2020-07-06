package com.dcj.meeting.controller;

import com.dcj.meeting.pojo.enterprise.UserIdentity;
import com.dcj.meeting.pojo.entity.History;
import com.dcj.meeting.pojo.entity.view.AppointmentView;
import com.dcj.meeting.pojo.feedback.Error;
import com.dcj.meeting.service.AppointmentService;
import com.dcj.meeting.util.AuthUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HistoryController {
    @Autowired
    AppointmentService appointmentService;

    @GetMapping("/history/{statusCode}")
    @ResponseBody
    public Object getHistory(@PathVariable("statusCode") Integer statusCode,
                             HttpSession session, Model model) {
        UserIdentity userIdentity = AuthUtil.takeUserIdentity(session);
        if (userIdentity == null) {
            return Error.invaildAccess();
        }
        List<AppointmentView> appointments = appointmentService.selectHistoryByStatusCode(userIdentity.getUserId(), statusCode);
        History history = new History();
        history.setStatusCode(statusCode);
        history.setHistory(appointments);
        return history;
    }

    @GetMapping("/history/detail/{appointId}")
    public String historyDetail(@PathVariable("appointId") Integer appointId,
                                Model model) {
        AppointmentView view = appointmentService.selectViewByAppointId(appointId);
        model.addAttribute("view", view);
        return "history_detail";
    }

}
