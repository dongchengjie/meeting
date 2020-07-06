package com.dcj.meeting.pojo.enterprise;

import java.util.List;

public class DepartmentList {
    private int errcode;
    private String errmsg;
    private List<Department> department;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public List<Department> getDepartment() {
        return department;
    }

    public void setDepartment(List<Department> department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "DepartmentList{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", department=" + department +
                '}';
    }
}
