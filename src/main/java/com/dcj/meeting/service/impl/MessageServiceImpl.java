package com.dcj.meeting.service.impl;

import com.dcj.meeting.pojo.enterprise.AccessToken;
import com.dcj.meeting.pojo.enterprise.message.UserTextMessage;
import com.dcj.meeting.pojo.feedback.Error;
import com.dcj.meeting.service.MessageService;
import com.dcj.meeting.util.MessageUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    AccessToken accessToken;

    @Override
    public boolean sendUserTextMessage(UserTextMessage userTextMessage) {
        Gson gson = new Gson();
        String json = gson.toJson(userTextMessage);
        String result = MessageUtil.sendMessage(accessToken.getAccess_token(), json);
        Error error = gson.fromJson(result, Error.class);
        return error.getErrcode() == 0;
    }
}
