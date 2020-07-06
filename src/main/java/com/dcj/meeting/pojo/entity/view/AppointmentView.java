package com.dcj.meeting.pojo.entity.view;

import java.util.Date;

public class AppointmentView {
    private int appointId;
    private String userid;
    private String place;
    private String purpose;
    private int participants;
    private String name;
    private String phone;
    private String note;
    private Date startTime;
    private int duration;
    private int statusCode;
    private String statusInfo;
    private String checkInfo;
    private Date handTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAppointId() {
        return appointId;
    }

    public void setAppointId(int appointId) {
        this.appointId = appointId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getPhone() {
        return phone;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

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

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

    public Date getHandTime() {
        return handTime;
    }

    public void setHandTime(Date handTime) {
        this.handTime = handTime;
    }

    @Override
    public String toString() {
        return "AppointmentView{" +
                "appointId=" + appointId +
                ", userid='" + userid + '\'' +
                ", place='" + place + '\'' +
                ", purpose='" + purpose + '\'' +
                ", participants=" + participants +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", note='" + note + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", statusCode=" + statusCode +
                ", statusInfo='" + statusInfo + '\'' +
                ", checkInfo='" + checkInfo + '\'' +
                ", handTime=" + handTime +
                '}';
    }
}
