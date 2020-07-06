package com.dcj.meeting.service;

import com.dcj.meeting.pojo.enterprise.message.UserTextMessage;

public interface MessageService {
    public boolean sendUserTextMessage(UserTextMessage userTextMessage);
}
