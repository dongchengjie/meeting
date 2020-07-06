package com.dcj.meeting.pojo.feedback;

public class Feedback {
    private int errcode = 0;
    private String errmsg = "ok";

    public Feedback() {

    }

    public Feedback(int errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

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
}
