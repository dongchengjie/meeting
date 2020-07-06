package com.dcj.meeting.pojo.entity;

import com.dcj.meeting.pojo.feedback.Feedback;

import java.util.ArrayList;
import java.util.List;

public class RoomPeriods extends Feedback {
    private int roomId;
    private List<Period> periods = new ArrayList<>();

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    @Override
    public String toString() {
        return "RoomPeriods{" +
                "roomId=" + roomId +
                ", periods=" + periods +
                '}';
    }
}
