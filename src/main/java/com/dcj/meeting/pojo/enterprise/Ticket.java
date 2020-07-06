package com.dcj.meeting.pojo.enterprise;

import org.springframework.stereotype.Component;

@Component
public class Ticket {
    private int errcode;
    private String errmsg;
    private String ticket;
    private int expiresIn;
    private long expireTime;

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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", ticket='" + ticket + '\'' +
                ", expiresIn=" + expiresIn +
                ", expireTime=" + expireTime +
                '}';
    }
}
