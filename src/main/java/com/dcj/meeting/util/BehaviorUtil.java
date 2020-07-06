package com.dcj.meeting.util;

import javax.servlet.http.HttpSession;

public class BehaviorUtil {
    //Session域存放key值
    private static final String FREQUENT_KEY = "lastSubmit";
    //频繁请求间隔（毫秒）
    private static final int REQUEST_INTERVAL = 5000;

    //判断是否频繁请求
    public static boolean isFrequentRequest(HttpSession session) {
        boolean frequent = false;
        Object lastSubmit = session.getAttribute(FREQUENT_KEY);
        if (lastSubmit != null) {
            if (System.currentTimeMillis() - (long) lastSubmit < REQUEST_INTERVAL) {
                frequent = true;
            }
        }
        session.setAttribute(FREQUENT_KEY, System.currentTimeMillis());
        return frequent;
    }


}
