package com.dcj.meeting.util;

import com.dcj.meeting.pojo.entity.Appointment;

import java.util.regex.Pattern;

public class CheckUtil {
    public static boolean isAppointmentInvalid(Appointment appointment) {
        //校验手机号码
        if (!invalidPhone(appointment.getPhone())) {
            return false;
        }
        //占用时长超过一天
        if (appointment.getDuration() > 1440) {
            return false;
        }
        return true;
    }

    //正则检查手机号码合理性
    private static boolean invalidPhone(String phone) {
        return Pattern.matches("^1[3456789]\\d{9}$", phone);
    }
}
