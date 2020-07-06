package com.dcj.meeting.controller;

import com.dcj.meeting.pojo.entity.*;
import com.dcj.meeting.pojo.feedback.Error;
import com.dcj.meeting.pojo.feedback.Feedback;
import com.dcj.meeting.pojo.server.OpenProperties;
import com.dcj.meeting.service.RoomService;
import com.dcj.meeting.util.BehaviorUtil;
import com.dcj.meeting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.dcj.meeting.service.impl.RoomServiceImpl.ROOM_DATA_FILEPATH;

@Controller
public class RoomController {
    @Autowired
    OpenProperties openProperties;
    @Autowired
    RoomService roomService;
    @Autowired
    File rootPath;

    @GetMapping("/room/{roomId}/{date}")
    public String getRoomInfo(@PathVariable("roomId") int roomId,
                              @PathVariable("date") long date,
                              Model model) {
        Room room = roomService.selectRoomById(roomId);
        model.addAttribute("room", room);
        model.addAttribute("date", date);
        return "room";
    }

    @GetMapping("/room/periods")
    @ResponseBody
    public RoomPeriods getRoomPeriods(Integer roomId, String type, long date) {
        RoomPeriods periods = roomService.selectRoomPeriods(roomId);
        if (type.equals("all")) {
            return periods;
        } else {
            RoomPeriods thatDay = new RoomPeriods();
            List<Period> list = periods.getPeriods();
            for (Period p : list) {
                if (DateUtil.isSameDay(date, p.getStartTime().getTime(), TimeZone.getDefault())) {
                    thatDay.getPeriods().add(p);
                }
            }
            return thatDay;
        }
    }

    @GetMapping("/rooms")
    @ResponseBody
    public RoomList getAvailableRooms() {//获取当前开放的会议室列表
        RoomList roomList = new RoomList();
        roomList.setRooms(roomService.selectAvailableRooms());
        roomList.sortByPlace();//按地点名升序排列
        return roomList;
    }

    //根据筛选条件，比对出合适的会议室
    @PostMapping("/rooms")
    @ResponseBody
    public Object getQualifiedRooms(int roomId, long date,
                                    long time, int duration,
                                    int size, HttpSession session) {
        //防止频繁提交
        if (BehaviorUtil.isFrequentRequest(session)) {
            return new Feedback(4003, "操作频繁");
        }
        double sh = openProperties.getStartHour();
        double ch = openProperties.getCloseHour();
        RoomList roomList = new RoomList();
        Date dDate = new Date(date);
        Date dTime = new Date(time);
        List<Room> rooms = new ArrayList<>();
        if (roomId == 0) {//查询出单个或所有会议室
            rooms = roomService.selectAvailableRooms();
        } else {
            rooms.add(roomService.selectRoomById(roomId));
        }
        //根据时间段是否明确，采用不同的查询方法
        if (time > 0) {//时间段明确
            Date combine = DateUtil.combineDateTime(dDate, dTime);
            Period period = new Period();
            period.setStartTime(combine);
            period.setDuration(duration);
            //一一比对届时是否空闲,同时在开放时间内
            for (Room room : rooms) {
                if (DateUtil.opening(sh, ch, period) && roomService.isAvailableDuring(room.getRoomId(), period) && room.getSize() >= size) {
                    //将空闲的一个或多个会议室加入列表
                    roomList.getRooms().add(room);
                }
            }
        } else {//时间段不明确，找出时长满足的空闲的单个或多个会议室
            roomList.getRooms().addAll(anyFreeRooms(rooms, dDate, duration, size));
        }
        roomList.sortByPlace();//按中文升序排列
        return roomList;
    }

    //判断会议室当天（介于会议室开放时间内）是否有空闲时间段，可供指定的时长占用
    public List<Room> anyFreeRooms(List<Room> rooms, Date date, int duration, int size) {
        double sh = openProperties.getStartHour();
        double ch = openProperties.getCloseHour();
        List<Room> freeRooms = new ArrayList<>();
        List<Period> sameDay = new ArrayList<>();
        //首先挑选出和预约同一天的时间段
        for (Room room : rooms) {
            //会议室大小不够
            if (room.getSize() < size) {
                continue;
            }
            sameDay.clear();//清空时间段
            //得到会议室所有占用时间段
            List<Period> periods = roomService.selectRoomPeriods(room.getRoomId()).getPeriods();
            //完全空闲，直接加入列表
            if (periods.size() == 0) {
                freeRooms.add(room);
                continue;
            }
            //挑选出和预约同一天的时间段
            for (Period period : periods) {
                if (DateUtil.isSameDay(period.getStartTime().getTime(), date.getTime(), TimeZone.getDefault())) {
                    sameDay.add(period);
                }
            }//会议室全天空闲，直接加入列表
            if (sameDay.size() == 0) {
                freeRooms.add(room);
                continue;
            }
            //分情况讨论开始时间
            for (Period period : sameDay) {
                Period test = new Period();
                test.setDuration(duration);
                //结束时间刚好是已知会议的开始时间,同时在开放时间内
                test.setStartTime(new Date(period.getStartTime().getTime() - duration * 60000));
                if (DateUtil.opening(sh, ch, test) && roomService.isAvailableDuring(room.getRoomId(), test)) {
                    freeRooms.add(room);
                    break;
                }
                //开始时间刚好是已知会议的结束时间,同时在开放时间内
                test.setStartTime(new Date(period.getStartTime().getTime() + period.getDuration() * 60000));
                if (DateUtil.opening(sh, ch, test) && roomService.isAvailableDuring(room.getRoomId(), test)) {
                    freeRooms.add(room);
                    break;
                }
            }
        }
        return freeRooms;
    }

    @PostMapping("/rooms/upload")
    @ResponseBody
    public Object uploadRoomsExcel(HttpSession session, MultipartFile rooms) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return new Feedback(1005, "管理员身份过期，请重新登录");
        }
        File path = new File(rootPath, ROOM_DATA_FILEPATH);
        try {
            rooms.transferTo(path);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"code\": 4004,\"msg\": \"表格文件上传失败\"}";
        }
        return "{\"code\": 0,\"msg\": \"表格文件上传成功\" ,\"data\": {\"filename\": \"" + rooms.getOriginalFilename() + "\"}}";
    }

    @GetMapping("/rooms/download")
    public String downloadRoomsExcel(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return Error.build(1005, "管理员身份过期，请重新登录", "/meeting/admin", "点击跳转", model);
        }
        int i = roomService.writeRoomDataToExcel();
        if (i > 0) {
            return "redirect:/rooms.xlsx";
        } else {
            return "redirect:/templates.xlsx";
        }
    }

    @GetMapping("/rooms/set")
    @ResponseBody
    public Object resetRooms(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return new Feedback(1005, "管理员身份过期，请重新登录");
        }
        int i = roomService.setRoomDataWithExcel();
        if (i > 0) {
            return new Feedback(0, "配置会议室成功");
        } else {
            return new Error(1012, "配置会议室数据失败，请检查上传的表格数据是否规范");
        }
    }

}
