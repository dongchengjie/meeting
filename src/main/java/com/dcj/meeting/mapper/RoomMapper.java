package com.dcj.meeting.mapper;

import com.dcj.meeting.pojo.entity.Room;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomMapper {
    @Select("select * from room")
    public List<Room> selectAll();

    @Select("select * from room where available=1")
    public List<Room> selectAvailableRooms();

    @Select("select * from room where room_id=#{roomId} limit 1")
    public Room selectByRoomId(int roomId);

    @Update("update room set place=#{place},size=#{size},available=#{available} where room_id=#{roomId}")
    public int updateRoom(Room room);

    @Insert("insert into room (place,size,available) values (#{place},#{size},#{available})")
    public int addRoom(Room room);

    @Delete("delete from room where room_id=#{roomId}")
    public int deleteRoom(Room room);

    @Delete("delete from room")
    public int deleteAll();
}
