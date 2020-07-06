package com.dcj.meeting.pojo.entity;

public class AppointmentStatus {
    public static final int OUT_OF_DATE = 0;
    public static final int CANCELED = 1;
    public static final int CHECKING = 2;
    public static final int PASSED = 3;
    public static final int REJECTED = 4;
    private int statusCode;
    private String statusInfo;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    @Override
    public String toString() {
        return "AppointmentStatus{" +
                "statusCode=" + statusCode +
                ", statusInfo='" + statusInfo + '\'' +
                '}';
    }
}
