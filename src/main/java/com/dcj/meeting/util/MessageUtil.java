package com.dcj.meeting.util;

public class MessageUtil {
    //发送消息的URL
    private static final String MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";

    public static String sendMessage(String accessToken, String json) {
        String url = MESSAGE_URL.replace("ACCESS_TOKEN", accessToken);
        return HttpUtil.postJson(url, json);
    }
}
