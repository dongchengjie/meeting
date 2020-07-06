package com.dcj.meeting.mapper;

import com.dcj.meeting.pojo.entity.view.CheckView;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckViewMapper {
    @Select("select * from check_view")
    public List<CheckView> selectAll();
}
