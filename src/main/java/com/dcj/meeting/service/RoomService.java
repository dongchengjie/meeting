package com.dcj.meeting.service;

import com.dcj.meeting.pojo.entity.Period;
import com.dcj.meeting.pojo.entity.Room;
import com.dcj.meeting.pojo.entity.RoomPeriods;

import java.util.List;

public interface RoomService {
    public List<Room> selectAvailableRooms();

    public boolean isAvailableDuring(int roomId, Period period);

    public List<RoomPeriods> selectAllRoomPeriods();

    public RoomPeriods selectRoomPeriods(int roomId);

    public Room selectRoomById(int roomId);

    public int setRoomDataWithExcel();

    public int writeRoomDataToExcel();
}
