package com.dcj.meeting.pojo.enterprise.message;

public class UserTextMessage {
    private String touser;
    private String msgtype;
    private int agentid;
    private Content text;

    public UserTextMessage() {
        this.msgtype = "text";
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Content getText() {
        return text;
    }

    public void setText(Content text) {
        this.text = text;
    }

    public String getContent() {
        return text.getContent();
    }

    public void setContent(String content) {
        if (this.text == null) {
            text = new Content();
        }
        this.text.setContent(content);
    }
}
