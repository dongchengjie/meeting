package com.dcj.meeting.service.impl;

import com.dcj.meeting.mapper.AppointmentMapper;
import com.dcj.meeting.mapper.RoomMapper;
import com.dcj.meeting.pojo.entity.Appointment;
import com.dcj.meeting.pojo.entity.Period;
import com.dcj.meeting.pojo.entity.Room;
import com.dcj.meeting.pojo.entity.RoomPeriods;
import com.dcj.meeting.service.RoomService;
import com.dcj.meeting.util.DateUtil;
import com.dcj.meeting.util.RoomExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {
    public static final String ROOM_TEMPLATE_FILEPATH = "static/template.xlsx";
    public static final String ROOM_DATA_FILEPATH = "static/rooms.xlsx";

    @Autowired
    AppointmentMapper appointmentMapper;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    File rootPath;

    //获得开放的会议室
    @Override
    public List<Room> selectAvailableRooms() {
        return roomMapper.selectAvailableRooms();
    }

    //会议室在指定时间段是否空闲
    @Override
    public boolean isAvailableDuring(int roomId, Period period) {
        RoomPeriods roomPeriods = selectRoomPeriods(roomId);
        if (roomPeriods == null) {
            return true;
        }
        List<Period> periods = roomPeriods.getPeriods();
        for (Period p : periods) {
            if (DateUtil.ifConflict(period, p)) {
                return false;
            }
        }
        return true;
    }

    //获得所有会议室的占用时间段
    @Override
    public List<RoomPeriods> selectAllRoomPeriods() {
        List<RoomPeriods> roomPeriodsList = new ArrayList<>();
        List<Appointment> appointments = appointmentMapper.selectAllPassedHistory();
        convertToRoomPeriodsList(appointments, roomPeriodsList);
        return roomPeriodsList;
    }

    //获得指定roomId会议室的占用时间段
    @Override
    public RoomPeriods selectRoomPeriods(int roomId) {
        List<RoomPeriods> roomPeriodsList = new ArrayList<>();
        List<Appointment> appointments = appointmentMapper.selectPassedHistory(roomId);
        convertToRoomPeriodsList(appointments, roomPeriodsList);
        RoomPeriods roomPeriods = new RoomPeriods();
        if (roomPeriodsList.size() > 0) {
            roomPeriods = roomPeriodsList.get(0);
        }
        return roomPeriods;
    }

    @Override
    public Room selectRoomById(int roomId) {
        return roomMapper.selectByRoomId(roomId);
    }

    @Override
    public int setRoomDataWithExcel() {
        File dataPath = new File(rootPath.getAbsolutePath(), ROOM_DATA_FILEPATH);
        if (dataPath.exists()) {
            List<Room> rooms = RoomExcelUtil.getRoomsFromExcel(dataPath.getPath());
            roomMapper.deleteAll();
            for (Room room : rooms) {
                roomMapper.addRoom(room);
            }
            return rooms.size();
        }
        return 0;
    }

    @Override
    public int writeRoomDataToExcel() {
        File in = new File(rootPath.getAbsolutePath(), ROOM_TEMPLATE_FILEPATH);
        if (in.exists()) {
            File out = new File(rootPath.getAbsolutePath(), ROOM_DATA_FILEPATH);
            List<Room> rooms = roomMapper.selectAll();
            return RoomExcelUtil.writeRoomsToExcel(in.getPath(), out.getPath(), rooms);
        }
        return 0;
    }

    //将占用记录封装进对象
    private void convertToRoomPeriodsList(List<Appointment> appointments, List<RoomPeriods> roomPeriodsList) {
        Map<Integer, List<Period>> map = new HashMap<>();
        for (Appointment appointment : appointments) {
            int r = appointment.getRoomId();
            Period period = new Period();
            period.setStartTime(appointment.getStartTime());
            period.setDuration(appointment.getDuration());
            List<Period> list;
            if (map.get(r) == null) {
                list = new ArrayList<>();
            } else {
                list = map.get(r);
            }
            list.add(period);
            map.put(r, list);
        }
        //封装进对象
        for (Integer key : map.keySet()) {
            RoomPeriods r = new RoomPeriods();
            r.setRoomId(key);
            r.setPeriods(map.get(key));
            roomPeriodsList.add(r);
        }
    }
}
