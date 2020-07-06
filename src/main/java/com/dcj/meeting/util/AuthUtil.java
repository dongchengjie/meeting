package com.dcj.meeting.util;

import com.dcj.meeting.pojo.enterprise.*;
import com.google.gson.Gson;

import javax.servlet.http.HttpSession;

public class AuthUtil {
    //获取access_token的URL
    private static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRET";
    //获取用户身份的URL
    private static final String USER_IDENTITY_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";
    //获取用户信息的URL
    private static final String USER_INFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";
    //获取jsapi_ticket的URL
    private static final String TICKET_URL = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";
    //二维码授权URL
    private static final String QRCODE_URL = "https://open.work.weixin.qq.com/wwopen/sso/qrConnect?appid=CORPID&agentid=AGENTID&redirect_uri=REDIRECT_URI&state=STATE";
    //二维码授权跳转地址
    private static final String REDIRECT_URL = "http://enterprise.viphk.ngrok.org/meeting/admin/qrcode/login";

    //JS-SDK使用权限签名算法
    public static String jsSdkSignatrue(String noncestr, String jsapi_ticket, String timestamp, String url) {
        String string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        return StringUtil.sha1(string1);
    }

    //通过corpid和corpsecret获取access_token
    public static AccessToken getAccessToken(String corpid, String corpsecret) {
        String url = ACCESS_TOKEN_URL.replace("ID", corpid).replace("SECRET", corpsecret);
        String result = HttpUtil.get(url);
        Gson gson = new Gson();
        AccessToken accessToken = gson.fromJson(result, AccessToken.class);
        if (accessToken.getErrcode() != 0) {
            return null;
        }
        accessToken.setExpireTime(System.currentTimeMillis() + accessToken.getExpires_in() * 1000);
        return accessToken;
    }

    //通过access_token和网页授权code获取用户信息
    public static UserIdentity getUserIdentity(String accessToken, String code) {
        String url = USER_IDENTITY_URL.replace("ACCESS_TOKEN", accessToken).replace("CODE", code);
        String result = HttpUtil.get(url);
        Gson gson = new Gson();
        UserIdentity userIdentity = gson.fromJson(result, UserIdentity.class);
        if (userIdentity.getErrcode() != 0) {
            return null;
        }
        return userIdentity;
    }

    //通过accessToken和userid获取用户信息
    public static UserInfo getUserInfo(String accessToken, String userid) {
        String url = USER_INFO_URL.replace("ACCESS_TOKEN", accessToken).replace("USERID", userid);
        String result = HttpUtil.get(url);
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(result, UserInfo.class);
        if (userInfo.getErrcode() != 0) {
            return null;
        }
        return userInfo;
    }

    //通过accessToken获取jsapi_ticket
    public static Ticket getTicket(String accessToken) {
        String url = TICKET_URL.replace("ACCESS_TOKEN", accessToken);
        String result = HttpUtil.get(url);
        Gson gson = new Gson();
        Ticket ticket = gson.fromJson(result, Ticket.class);
        if (ticket.getErrcode() != 0) {
            return null;
        }
        ticket.setExpireTime(System.currentTimeMillis() + ticket.getExpiresIn() * 1000);
        return ticket;
    }

    public static void putUserIdentity(UserIdentity userIdentity, HttpSession session) {
        session.setAttribute("userIdentity", userIdentity);
    }

    public static UserIdentity takeUserIdentity(HttpSession session) {
        return (UserIdentity) session.getAttribute("userIdentity");
    }

    public static String buildQrCodeUrl(AppProperties appProperties) {
        String url = QRCODE_URL.replace("CORPID", appProperties.getCorpid()).replace("AGENTID", appProperties.getAgentid()).replace("STATE", "state");
        return url.replace("REDIRECT_URI", StringUtil.urlencode(REDIRECT_URL, "utf-8"));
    }
}
