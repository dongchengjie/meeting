package com.dcj.meeting.util;

import com.dcj.meeting.pojo.entity.Period;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    //判断两个时间段是否冲突
    public static boolean ifConflict(Date date1, int duration1, Date date2, int duration2) {
        long start1 = date1.getTime();
        long start2 = date2.getTime();
        return start2 + duration2 * 60000 > start1 && start2 < start1 + duration1 * 60000;
    }

    //判断两个Period是否冲突
    public static boolean ifConflict(Period p1, Period p2) {
        return ifConflict(p1.getStartTime(), p1.getDuration(), p2.getStartTime(), p2.getDuration());
    }

    public static boolean isSameDay(long millis1, long millis2, TimeZone timeZone) {
        long interval = millis1 - millis2;
        return interval < 86400000 && interval > -86400000 && millis2Days(millis1, timeZone) == millis2Days(millis2, timeZone);
    }

    private static long millis2Days(long millis, TimeZone timeZone) {
        return (((long) timeZone.getOffset(millis)) + millis) / 86400000;
    }

    //将日期信息和时间信息合并
    public static Date combineDateTime(Date date, Date time) {
        Calendar cDate = Calendar.getInstance(TimeZone.getDefault());
        cDate.setTime(date);
        Calendar cTime = Calendar.getInstance(TimeZone.getDefault());
        cTime.setTime(time);
        cDate.set(Calendar.HOUR_OF_DAY, cTime.get(Calendar.HOUR_OF_DAY));
        cDate.set(Calendar.MINUTE, cTime.get(Calendar.MINUTE));
        cDate.set(Calendar.SECOND, 0);
        return cDate.getTime();
    }

    //判断会议室是否开放
    public static boolean opening(double startHour, double closeHour, Period period) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        //开始时间
        calendar.setTime(period.getStartTime());
        int sd = calendar.get(Calendar.DAY_OF_MONTH);
        int sh = calendar.get(Calendar.HOUR_OF_DAY);
        double sm = calendar.get(Calendar.MINUTE) / 60.0;
        //关闭时间
        calendar.setTime(new Date(period.getStartTime().getTime() + period.getDuration() * 60000));
        if (calendar.get(Calendar.DAY_OF_MONTH) != sd) {
            return false;//经过duration后不是同一天了,duration过大
        }
        int ch = calendar.get(Calendar.HOUR_OF_DAY);
        double cm = calendar.get(Calendar.MINUTE) / 60.0;
        return sh + sm >= startHour && ch + cm <= closeHour;
    }

    public static boolean opening(double startHour, double closeHour, Date startTime, int duration) {
        Period period = new Period();
        period.setStartTime(startTime);
        period.setDuration(duration);
        return opening(startHour, closeHour, period);
    }

    //构建时间段
    public static Period buildPeriod(Date startTime, int duration) {
        Period period = new Period();
        period.setStartTime(startTime);
        period.setDuration(duration);
        return period;
    }

    //获取今日凌晨日期
    public static Date getTodayStart() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    //判断记录是否过期
    public static boolean ifOutOfDate(Date startTime, int duration) {
        Date endTime = new Date(startTime.getTime() + duration * 60 * 1000);
        return System.currentTimeMillis() > endTime.getTime();
    }
}
