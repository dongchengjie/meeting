package com.dcj.meeting.pojo.entity.view;

import java.util.Date;

public class CheckView {
    private int appointId;
    private String userid;
    private String name;
    private int roomId;
    private String place;
    private int size;
    private String purpose;
    private int participants;
    private String phone;
    private Date startTime;
    private int duration;
    private String note;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getHandTime() {
        return handTime;
    }

    public void setHandTime(Date handTime) {
        this.handTime = handTime;
    }

    @Override
    public String toString() {
        return "CheckView{" +
                "appointId=" + appointId +
                ", userid='" + userid + '\'' +
                ", name='" + name + '\'' +
                ", roomId=" + roomId +
                ", place='" + place + '\'' +
                ", size=" + size +
                ", purpose='" + purpose + '\'' +
                ", participants=" + participants +
                ", phone='" + phone + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", note='" + note + '\'' +
                ", handTime=" + handTime +
                '}';
    }
}
