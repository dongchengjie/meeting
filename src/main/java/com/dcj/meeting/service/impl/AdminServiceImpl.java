package com.dcj.meeting.service.impl;

import com.dcj.meeting.mapper.AdminMapper;
import com.dcj.meeting.pojo.entity.Admin;
import com.dcj.meeting.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(Admin admin) {
        if (validAdmin(admin)) {
            return adminMapper.selectAdminByUsername(admin.getUsername());
        }
        return null;
    }

    @Override
    public boolean validAdmin(Admin admin) {
        Admin test = adminMapper.selectAdminByUsername(admin.getUsername());
        return test != null && test.getPassword().equals(admin.getPassword());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addAdmin(Admin operator, Admin admin) {
        if (operator.getPrivileged() == 1) {
            if (existsAdmin(admin.getUsername())) {
                return 0;
            }
            return adminMapper.addAdmin(admin);
        }
        return -1;
    }

    @Override
    public boolean existsAdmin(String username) {
        return adminMapper.selectAdminByUsername(username) != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAdmin(Admin operator, int id) {
        if (operator.getPrivileged() == 1) {
            return adminMapper.deleteAdmin(id);
        }
        return 0;
    }

    @Override
    public List<Admin> selectAll() {
        return adminMapper.selectAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAdmin(Admin operator, Admin admin) {
        if (operator.getPrivileged() == 1 || operator.getId() == admin.getId()) {
            return adminMapper.updateAdmin(admin);
        }
        return -1;

    }

    @Override
    public Admin selectById(int id) {
        return adminMapper.selectAdminById(id);
    }

    @Override
    public Admin selectAdminByUsername(String username) {
        return adminMapper.selectAdminByUsername(username);
    }
}
