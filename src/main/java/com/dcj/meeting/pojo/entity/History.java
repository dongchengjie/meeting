package com.dcj.meeting.pojo.entity;

import com.dcj.meeting.pojo.entity.view.AppointmentView;
import com.dcj.meeting.pojo.feedback.Feedback;

import java.util.ArrayList;
import java.util.List;

public class History extends Feedback {
    private int statusCode;
    private List<AppointmentView> history = new ArrayList<>();

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<AppointmentView> getHistory() {
        return history;
    }

    public void setHistory(List<AppointmentView> history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return "History{" +
                "statusCode=" + statusCode +
                ", history=" + history +
                '}';
    }
}
