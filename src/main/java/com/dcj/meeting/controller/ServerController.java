package com.dcj.meeting.controller;

import com.dcj.meeting.pojo.entity.Admin;
import com.dcj.meeting.pojo.feedback.Error;
import com.dcj.meeting.pojo.feedback.Feedback;
import com.dcj.meeting.pojo.server.AutoCheckProperties;
import com.dcj.meeting.pojo.server.AutoCleanProperties;
import com.dcj.meeting.pojo.server.OpenProperties;
import com.dcj.meeting.service.AppointmentService;
import com.dcj.meeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ServerController {
    @Autowired
    UserService userService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    AutoCheckProperties autoCheckProperties;
    @Autowired
    OpenProperties openProperties;
    @Autowired
    AutoCleanProperties autoCleanProperties;

    @GetMapping("/server")
    @ResponseBody
    public Object getServerInfo(HttpSession session) {
        Map<String, Object> serverInfo = new HashMap<>();
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return new Error(1005, "管理员身份过期，请重新登录");
        }
        //管理员用户名
        String username = admin.getUsername();
        //用户数
        int totalUser = userService.selectTotal();
        //总预约数
        int totalAppointment = appointmentService.selectTotal();
        //今日预约数
        int totalToday = appointmentService.selectTodayTotal();
        //自动审核运行状态
        boolean autoCheck = autoCheckProperties.isEnabled();
        //自动清理过期记录运行状态
        boolean autoClean = autoCleanProperties.isEnabled();
        //放入数据
        serverInfo.put("username", username);
        serverInfo.put("totalUser", totalUser);
        serverInfo.put("totalAppointment", totalAppointment);
        serverInfo.put("totalToday", totalToday);
        serverInfo.put("autoCheck", autoCheck);
        serverInfo.put("autoClean", autoClean);
        serverInfo.put("errcode", 0);
        serverInfo.put("errmsg", "ok");
        return serverInfo;
    }

    @GetMapping("/server/properties")
    @ResponseBody
    public Object getServerProperties() {
        Map<String, Object> properties = new HashMap<>();
        //会议室开放时间
        double startHour = openProperties.getStartHour();
        double closeHour = openProperties.getCloseHour();
        int sh = (int) startHour;
        int sm = (int) ((startHour - sh) * 60);
        int ch = (int) closeHour;
        int cm = (int) ((closeHour - ch) * 60);
        //自动审核参数
        boolean check = autoCheckProperties.isEnabled();
        int checkInterval = autoCheckProperties.getCheckInterval();
        //自动清理参数
        boolean clean = autoCleanProperties.isEnabled();
        int cleanInterval = autoCleanProperties.getCleanInterval();
        //放入数据
        properties.put("sh", sh);
        properties.put("sm", sm);
        properties.put("ch", ch);
        properties.put("cm", cm);
        properties.put("check", check);
        properties.put("checkInterval", checkInterval);
        properties.put("clean", clean);
        properties.put("cleanInterval", cleanInterval);
        properties.put("errcode", 0);
        properties.put("errmsg", "ok");
        return properties;
    }

    @PostMapping("/server/properties")
    @ResponseBody
    public Object setServerProperties(Integer sh, Integer sm,
                                      Integer ch, Integer cm,
                                      boolean check, Integer checkInterval,
                                      boolean clean, Integer cleanInterval) {
        int target = 3;
        int count = 0;
        try {
            //设置开放时间
            if (OpenProperties.invalidOpenTime(sh, sm, ch, cm)) {
                openProperties.setStartHour(sh + sm / 60.0);
                openProperties.setCloseHour(ch + cm / 60.0);
                count++;
            }
            //设置自动审核
            if (!(checkInterval < 0)) {
                autoCheckProperties.setEnabled(check);
                autoCheckProperties.setCheckInterval(checkInterval);
                count++;
            }
            //设置自动清理过期记录
            if (!(cleanInterval < 0)) {
                autoCleanProperties.setEnabled(clean);
                autoCleanProperties.setCleanInterval(cleanInterval);
                count++;
            }
        } catch (Exception e) {
            return new Error(1003, "服务器属性修改失败");
        }
        if (count < target) {
            return new Feedback(1004, "部分服务器属性修改失败，请检查数据格式是否规范");
        } else {
            return new Feedback(0, "修改成功");
        }
    }
}
