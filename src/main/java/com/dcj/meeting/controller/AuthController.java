package com.dcj.meeting.controller;

import com.dcj.meeting.pojo.enterprise.*;
import com.dcj.meeting.pojo.feedback.Error;
import com.dcj.meeting.service.UserService;
import com.dcj.meeting.util.AuthUtil;
import com.dcj.meeting.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {
    @Autowired
    AppProperties appProperties;
    @Autowired
    AccessToken accessToken;
    @Autowired
    Ticket ticket;
    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/auth")
    public String auth(String code, String state, HttpSession session, Model model) {
        if (code == null) {
            return Error.build(3001, "请在企业微信客户端打开链接", model);
        }
        //获取用户身份
        UserIdentity userIdentity = AuthUtil.getUserIdentity(accessToken.getAccess_token(), code);
        logger.info("用户身份:" + userIdentity);
        if (!userIdentity.isMemberOfEnterprise()) {
            return Error.build(3002, "非企业成员", model);
        }
        //将用户身份放入session中
        AuthUtil.putUserIdentity(userIdentity, session);
        //检查用户是否已在数据库中
        if (!userService.ifExistsUser(userIdentity.getUserId())) {
            logger.info("用户不在数据库中，将创建新用户，并同步资料");
            UserInfo userInfo = AuthUtil.getUserInfo(accessToken.getAccess_token(), userIdentity.getUserId());
            if (userInfo == null) {
                return Error.build(1001, "获取企业内用户信息失败", model);
            }
            userService.createUser(userIdentity.getUserId(), userInfo);
        }
        return "redirect:/index";
    }

    @GetMapping("/ticket")
    @ResponseBody
    public String getTicket() {
        if (ticket.isExpired() || ticket.getTicket() == null) {
            Ticket newTicket = AuthUtil.getTicket(accessToken.getAccess_token());
            logger.info("获取ticket成功:" + newTicket.getTicket());
            ticket.setTicket(newTicket.getTicket());
            ticket.setExpiresIn(newTicket.getExpiresIn());
            ticket.setExpireTime(newTicket.getExpireTime());
        }
        return ticket.getTicket();
    }

    //获取JS-SDK使用权限签名
    @RequestMapping("/jssdk")
    @ResponseBody
    public JsConfig jsSdkSignature(String url) {
        String noncestr = StringUtil.randomString(10);
        String jsapi_ticket = getTicket();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String signature = AuthUtil.jsSdkSignatrue(noncestr, jsapi_ticket, timestamp, url);
        JsConfig jsConfig = new JsConfig(appProperties.getCorpid(), Integer.valueOf(timestamp), noncestr, signature);
        logger.info("JS-SDK使用权限签名:" + jsConfig.toString());
        return jsConfig;
    }
}
