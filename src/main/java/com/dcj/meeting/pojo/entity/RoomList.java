package com.dcj.meeting.pojo.entity;

import com.dcj.meeting.pojo.feedback.Feedback;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoomList extends Feedback {
    private List<Room> rooms = new ArrayList<>();

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void sortByPlace() {
        if (rooms != null && rooms.size() > 1) {
            rooms.sort((o1, o2) -> Collator.getInstance(Locale.CHINESE).compare(o1.getPlace(), o2.getPlace()));
        }
    }
}
