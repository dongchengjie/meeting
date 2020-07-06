package com.dcj.meeting.pojo.enterprise;

public class UserIdentity {
    private int errcode;
    private String errmsg;
    private String UserId;
    private String DeviceId;
    private String OpenId;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getOpenId() {
        return OpenId;
    }

    public void setOpenId(String openId) {
        OpenId = openId;
    }

    public boolean isMemberOfEnterprise() {
        return OpenId == null || UserId != null;
    }

    @Override
    public String toString() {
        return "UserIdentity{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", UserId='" + UserId + '\'' +
                ", DeviceId='" + DeviceId + '\'' +
                ", OpenId='" + OpenId + '\'' +
                '}';
    }
}
