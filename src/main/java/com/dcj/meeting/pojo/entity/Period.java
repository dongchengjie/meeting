package com.dcj.meeting.pojo.entity;

import java.util.Date;

public class Period {
    private Date startTime;
    private int duration;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Period{" +
                "startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
