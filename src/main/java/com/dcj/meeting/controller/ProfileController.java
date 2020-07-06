package com.dcj.meeting.controller;

import com.dcj.meeting.pojo.enterprise.AccessToken;
import com.dcj.meeting.pojo.enterprise.UserIdentity;
import com.dcj.meeting.pojo.entity.Admin;
import com.dcj.meeting.pojo.entity.LayuiTable;
import com.dcj.meeting.pojo.entity.Profile;
import com.dcj.meeting.pojo.entity.UserInfoItem;
import com.dcj.meeting.pojo.feedback.Error;
import com.dcj.meeting.pojo.feedback.Feedback;
import com.dcj.meeting.service.ProfileService;
import com.dcj.meeting.service.UserService;
import com.dcj.meeting.util.AuthUtil;
import com.dcj.meeting.util.ImageUtil;
import com.dcj.meeting.util.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    ProfileService profileService;
    @Autowired
    UserService userService;
    @Autowired
    AccessToken accessToken;
    @Autowired
    File rootPath;

    @GetMapping("/profile")
    @ResponseBody
    public Object getProfile(HttpSession session) {
        UserIdentity userIdentity = AuthUtil.takeUserIdentity(session);
        if (userIdentity == null) {
            return Error.invaildAccess();
        } else if (!userIdentity.isMemberOfEnterprise()) {
            return Error.notMenerOfEnterprise();
        } else {
            Profile profile = profileService.selectProfileByUserid(userIdentity.getUserId());
            if (profile == null) {
                return new Error(2001, "获取用户信息失败");
            }
            return profile;
        }
    }

    @PostMapping("/profile")
    @ResponseBody
    public Object updateProfile(HttpSession session,
                                @RequestParam(defaultValue = "0") int gender,
                                @RequestParam(defaultValue = "0") int age,
                                String avatar, String phone, String email) {
        UserIdentity userIdentity = AuthUtil.takeUserIdentity(session);
        Profile profile = profileService.selectProfileByUserid(userIdentity.getUserId());
        profile.setAvatar(avatar);
        profile.setGender(gender);
        profile.setAge(age);
        profile.setPhone(phone);
        profile.setEmail(email);
        if (profileService.updateProfile(profile) > 0) {
            return profileService.selectProfileByUserid(userIdentity.getUserId());
        } else {
            return new Error(2002, "修改用户信息失败");
        }
    }

    @PostMapping("/profile/upload")
    @ResponseBody
    public Object uploadAvatar(HttpServletRequest request, HttpSession session, MultipartFile upload) {
        File path = new File(rootPath.getAbsolutePath(), "static/avatar");
        if (!path.exists()) {
            path.mkdirs();
        }
        String originalFilename = upload.getOriginalFilename();
        //文件后缀
        String suffix = StringUtil.fileSuffix(originalFilename);
        //判断上传的是不是图片
        if (!ImageUtil.isSupported(suffix)) {
            return new Feedback(4001, "您上传头像的不是图片类型");
        }
        //保存的文件名
        String saveName = StringUtil.uuid() + suffix;
        File file = new File(path, saveName);
        try {
            upload.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //图片压缩
        ImageUtil.compress(file, 0.7);
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/meeting/avatar/" + saveName;
        return new Feedback(0, url);
    }

    @GetMapping("/profile/all")
    @ResponseBody
    public Object profileManage(HttpSession session, Integer page, Integer size) {
        LayuiTable<UserInfoItem> profileTable = new LayuiTable<>();
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return profileTable;
        }
        //分页
        Page<Object> objectPage = PageHelper.startPage(page, size);
        List<Profile> profiles = profileService.selectAll();
        profileTable.setCount(objectPage.getTotal());
        List<UserInfoItem> data = profileTable.getData();
        for (Profile profile : profiles) {
            data.add(UserInfoItem.build(profile, userService.selectById(profile.getId())));
        }
        return profileTable;
    }
}
