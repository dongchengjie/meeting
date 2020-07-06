package com.dcj.meeting.pojo.feedback;

import org.springframework.ui.Model;

public class Error extends Feedback {

    public Error() {

    }

    public Error(int errcode, String errmsg) {
        super(errcode, errmsg);
    }

    //默认视图名为error
    public static String build(int errcode, String errmsg, Model model) {
        Error error = new Error(errcode, errmsg);
        model.addAttribute("error", error);
        return "error";
    }

    //带跳转链接的错误页面
    public static String build(int errcode, String errmsg, String redirectUrl, String redirectMsg, Model model) {
        Error error = new Error(errcode, errmsg);
        model.addAttribute("error", error);
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("redirectMsg", redirectMsg);
        return "error";
    }

    //指定视图名
    public static String build(int errcode, String errmsg, String path, Model model) {
        Error error = new Error(errcode, errmsg);
        model.addAttribute("error", error);
        return path;
    }

    public static Error invaildAccess() {
        return new Error(3001, "请在企业微信客户端打开链接");
    }

    public static Error notMenerOfEnterprise() {
        return new Error(3002, "非企业成员");
    }
}
