package com.dcj.meeting.service;

import com.dcj.meeting.pojo.entity.Admin;

import java.util.List;

public interface AdminService {
    public Admin login(Admin admin);

    public boolean validAdmin(Admin admin);

    public int addAdmin(Admin operator, Admin admin);

    public boolean existsAdmin(String username);

    public int deleteAdmin(Admin operator, int id);

    public List<Admin> selectAll();

    public int updateAdmin(Admin operator, Admin admin);

    public Admin selectById(int id);

    public Admin selectAdminByUsername(String username);
}
