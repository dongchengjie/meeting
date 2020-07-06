package com.dcj.meeting.pojo.entity;

import java.util.Date;

public class Appointment {
    private int appointId;
    private String userid;
    private int roomId;
    private String purpose;
    private int participants;
    private String phone;
    private String note;
    private Date startTime;
    private int duration;
    private int statusCode;
    private String checkInfo;
    private Date handTime;

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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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
        return "Appointment{" +
                "appointId=" + appointId +
                ", userid='" + userid + '\'' +
                ", roomId=" + roomId +
                ", purpose='" + purpose + '\'' +
                ", participants=" + participants +
                ", phone='" + phone + '\'' +
                ", note='" + note + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", statusCode=" + statusCode +
                ", checkInfo='" + checkInfo + '\'' +
                ", handTime=" + handTime +
                '}';
    }

    public void setChecking(String userid, Integer roomId, Long datetime,
                            Integer duration, Integer size, String phone,
                            String purpose, String note) {
        this.setUserid(userid);
        this.setRoomId(roomId);
        this.setStartTime(new Date(datetime));
        this.setDuration(duration);
        this.setParticipants(size);
        this.setPhone(phone);
        this.setPurpose(purpose);
        this.setNote(note);
        this.setHandTime(new Date(System.currentTimeMillis()));
        this.setStatusCode(AppointmentStatus.CHECKING);
        this.setCheckInfo("审核中，请耐心等待");
    }
}
