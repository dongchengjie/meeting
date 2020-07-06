use mysql;
create database meeting;
use meeting;
#user（用户表）
create table user(
    id int primary key auto_increment,        #用户唯一标识id
    userid varchar(20) not null unique,        #企业成员userid
    create_time timestamp not null            #账号创建时间
);
#admin(管理员表)
create table admin(
    id int primary key auto_increment,        #管理员id
    username varchar(20) unique,            #管理员用户名
    password varchar(20) not null,            #管理员密码
    privileged int not null,                #是否为超级管理员
    create_time timestamp not null            #管理员账号创建时间
);
#profile（用户信息表）
create table profile(
    id int primary key,                    #用户唯一标识
    role int not null,                    #身份（学生1或老师2）
    userid varchar(20) not null,        #学号或职工号
    avatar varchar(255) not null,        #用户头像（未设置给定默认头像）
    name varchar(10),                    #名字
    age int,                            #年龄
    gender int not null,                #性别
    phone varchar(20),                    #电话
    email varchar(20)                    #邮箱
);
#room（会议室信息表）
create table room(
    room_id int primary key auto_increment,    #会议室唯一标识
    place varchar(30) not null unique,        #会议室地点
    size int not null,                        #会议室可容纳人数
    available int not null                    #会议室当前是否开放        
);
#appointment（预约记录表）
create table appointment(
    appoint_id int primary key auto_increment,    #预约记录id
    userid varchar(20) not null,                #预约人（外键user.userid）
    room_id int not null,                        #预约会议室id
    purpose varchar(30),                        #会议室用途
    participants int not null,                    #参与人数
    phone varchar(20) not null,                    #联系电话
    note varchar(50),                            #备注信息
    start_time timestamp not null,                #占用开始时间
    duration int not null,                        #占用时长（单位：分钟）
    status_code int not null,                    #审核状态码
    check_info varchar(30),                        #审核信息
    hand_time timestamp    not null                #预约提交时间
);
#appointment_status（审核状态表）
create table appointment_status(
    status_code int primary key,                #状态码
    status_info varchar(10) not null            #状态信息
);
#check_view（管理员审核视图）
create view check_view as select
    appoint_id,
    hand_time,
    user.userid,
    name,
    appointment.room_id,
    place,
    purpose,
    start_time,
    duration,
    participants,
    size,
    appointment.phone,
    appointment.note
from
    appointment,
    user,
    profile,
    room
where
    user.userid = profile.userid
    and user.userid = appointment.userid
    and appointment.room_id = room.room_id
    and room.available = 1
    and appointment.status_code = 2
order by
    hand_time asc;
#appointment_view（查看预约记录视图）
create view appointment_view as select
    appointment.appoint_id,
    appointment.userid,
    room.place,
    appointment.purpose,
    appointment.participants,
    profile.name,
    appointment.phone,
    appointment.note,
    appointment.start_time,
    appointment.duration,
    appointment_status.status_code,
    appointment_status.status_info,
    appointment.check_info,
    hand_time
from
    appointment,
    room,
    appointment_status,
    profile
where
    appointment.room_id = room.room_id
    and appointment.status_code = appointment_status.status_code
    and appointment.userid = profile.userid
order by
    hand_time desc;
##########插入初始数据##########
#插入状态码数据
insert into appointment_status (status_code,status_info) values (0,'预约过期');
insert into appointment_status (status_code,status_info) values (1,'预约被取消');
insert into appointment_status (status_code,status_info) values (2,'审核中');
insert into appointment_status (status_code,status_info) values (3,'审核通过');
insert into appointment_status (status_code,status_info) values (4,'审核退回');
#插入超级管理员账号
insert into admin (username,password,privileged,create_time) values ('admin','admin',1,now());
#测试数据
insert into room (place,size,available) values ('逸夫楼101',50,1);
insert into room (place,size,available) values ('逸夫楼201',40,1);
insert into room (place,size,available) values ('逸夫楼301',35,1);
insert into room (place,size,available) values ('逸夫楼401',55,1);
insert into room (place,size,available) values ('逸夫楼501',45,1);