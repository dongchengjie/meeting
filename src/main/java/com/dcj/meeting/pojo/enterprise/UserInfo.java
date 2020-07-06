package com.dcj.meeting.pojo.enterprise;

public class UserInfo {
    private int errcode;
    private String errmsg;
    private String userid;              //成员UserID
    private String name;                //成员名称
    private int[] department;           //成员所属部门id列表
    private String position;            //职务信息
    private String mobile;              //手机号码
    private int gender;                 //性别
    private String email;               //电子邮箱
    private String avatar;              //头像url
    private int status;                 //激活状态: 1=已激活，2=已禁用，4=未激活。
    private int isleader;
    private String telephone;           //座机
    private int enable;                 //成员启用状态。1表示启用的成员，0表示被禁用
    private int hide_mobile;
    private int[] order;               //部门内的排序值，默认为0。
    private String qr_code;             //员工个人二维码url
    private String alias;               //别名
    private int[] is_leader_in_dept;   //在所在的部门内是否为上级
    private String thumb_avatar;        //头像缩略图url
    private String address;             //地址

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

    public int[] getIs_leader_in_dept() {
        return is_leader_in_dept;
    }

    public void setIs_leader_in_dept(int[] is_leader_in_dept) {
        this.is_leader_in_dept = is_leader_in_dept;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getDepartment() {
        return department;
    }

    public void setDepartment(int[] department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsleader() {
        return isleader;
    }

    public void setIsleader(int isleader) {
        this.isleader = isleader;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getHide_mobile() {
        return hide_mobile;
    }

    public void setHide_mobile(int hide_mobile) {
        this.hide_mobile = hide_mobile;
    }

    public void setOrder(int[] order) {
        this.order = order;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int[] getOrder() {
        return order;
    }

    public String getThumb_avatar() {
        return thumb_avatar;
    }

    public void setThumb_avatar(String thumb_avatar) {
        this.thumb_avatar = thumb_avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userid='" + userid + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", mobile='" + mobile + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", status=" + status +
                ", isleader=" + isleader +
                ", telephone='" + telephone + '\'' +
                ", enable=" + enable +
                ", hide_mobile=" + hide_mobile +
                ", order='" + order + '\'' +
                ", qr_code='" + qr_code + '\'' +
                ", alias='" + alias + '\'' +
                ", is_leader_in_dept='" + is_leader_in_dept + '\'' +
                ", thumb_avatar='" + thumb_avatar + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
