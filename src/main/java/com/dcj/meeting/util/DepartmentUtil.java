package com.dcj.meeting.util;

import com.dcj.meeting.pojo.enterprise.DepartmentList;
import com.google.gson.Gson;

public class DepartmentUtil {
    //获取部门信息地URL
    private static final String GET_DEPARTMENT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN&id=ID";

    //获取所有部门
    public static DepartmentList getAllDepartments(String accessToken) {
        String url = GET_DEPARTMENT_URL.replace("ACCESS_TOKEN&id=ID", accessToken);
        String result = HttpUtil.get(url);
        Gson gson = new Gson();
        DepartmentList departmentList = gson.fromJson(result, DepartmentList.class);
        if (departmentList != null && departmentList.getErrcode() == 0) {
            return departmentList;
        }
        return null;
    }

    //获取指定部门及其下的子部门
    public static DepartmentList getAllDepartments(String accessToken, int id) {
        String url = GET_DEPARTMENT_URL.replace("ACCESS_TOKEN", accessToken).replace("ID", String.valueOf(id));
        String result = HttpUtil.get(url);
        Gson gson = new Gson();
        DepartmentList departmentList = gson.fromJson(result, DepartmentList.class);
        if (departmentList != null && departmentList.getErrcode() == 0) {
            return departmentList;
        }
        return null;
    }
}
