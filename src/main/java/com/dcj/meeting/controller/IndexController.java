package com.dcj.meeting.controller;

import com.dcj.meeting.pojo.enterprise.AccessToken;
import com.dcj.meeting.pojo.enterprise.UserIdentity;
import com.dcj.meeting.pojo.feedback.Error;
import com.dcj.meeting.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    AccessToken accessToken;

    @RequestMapping({"/index", "/", "/index.html"})
    public String index(HttpSession session, Model model) {
        UserIdentity userIdentity = AuthUtil.takeUserIdentity(session);
        if (userIdentity == null) {
            return Error.build(3001, "请在企业微信客户端打开链接", model);
        }
        return "redirect:/pages/index.html";
    }
}
