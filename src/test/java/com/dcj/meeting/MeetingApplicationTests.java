package com.dcj.meeting;

import com.dcj.meeting.pojo.entity.Room;
import com.dcj.meeting.pojo.server.OpenProperties;
import com.dcj.meeting.service.AdminService;
import com.dcj.meeting.service.RoomService;
import com.dcj.meeting.util.RoomExcelUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MeetingApplicationTests {

    @Autowired
    RoomService roomService;
    @Autowired
    OpenProperties openProperties;
    @Autowired
    AdminService adminService;

    @Test
    void contextLoads() {

    }


}
