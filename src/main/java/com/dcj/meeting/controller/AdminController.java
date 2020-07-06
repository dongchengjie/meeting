package com.dcj.meeting.controller;

import com.dcj.meeting.pojo.enterprise.AccessToken;
import com.dcj.meeting.pojo.enterprise.AppProperties;
import com.dcj.meeting.pojo.enterprise.UserIdentity;
import com.dcj.meeting.pojo.entity.Admin;
import com.dcj.meeting.pojo.entity.LayuiTable;
import com.dcj.meeting.pojo.feedback.Error;
import com.dcj.meeting.pojo.feedback.Feedback;
import com.dcj.meeting.service.AdminService;
import com.dcj.meeting.service.ProfileService;
import com.dcj.meeting.util.AuthUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    ProfileService profileService;

    @Autowired
    AccessToken accessToken;
    @Autowired
    AppProperties appProperties;

    @GetMapping("/admin")
    public String dashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/pages/login.html";
        }
        model.addAttribute("admin", admin);
        return "dashboard";
    }

    @GetMapping("/admin/qrcode")
    public String qrcode() {
        return "redirect:" + AuthUtil.buildQrCodeUrl(appProperties);
    }

    @GetMapping("/admin/qrcode/login")
    public String qrcodeLogin(String code, String state, Model model, HttpSession session) {
        if (code == null) {
            return Error.build(3001, "请在企业微信客户端打开链接", model);
        }
        //获取用户身份
        UserIdentity userIdentity = AuthUtil.getUserIdentity(accessToken.getAccess_token(), code);
        if (userIdentity == null) {
            return Error.build(3004, "身份信息过期", "/meeting/admin", "点击回到登录界面", model);
        }
        if (!userIdentity.isMemberOfEnterprise()) {
            return Error.build(3002, "非企业成员", model);
        }
        String userId = userIdentity.getUserId();
        Admin admin = adminService.selectAdminByUsername(userId);
        if (admin == null) {
            return Error.build(1010, "未找到企业用户名和管理员名相同的匹配项（非管理员）", "/meeting/admin", "点击回到登录界面", model);
        }
        session.setAttribute("admin", admin);
        return "redirect:/admin";
    }

    @PostMapping("/login")
    @ResponseBody
    public Object login(Admin admin, HttpSession session) {
        Admin login = adminService.login(admin);
        if (login != null) {
            session.setAttribute("admin", login);
            return new Feedback(0, "登录成功");
        }
        return new Error(1002, "管理员用户名或密码错误");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute("admin", null);
        return "redirect:/admin";
    }

    @GetMapping("/admin/all")
    @ResponseBody
    public Object getAdmins(Integer page, Integer size, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return new Feedback(1005, "管理员身份过期，请重新登录");
        }
        LayuiTable<Admin> admins = new LayuiTable<>();
        //分页
        Page<Object> objectPage = PageHelper.startPage(page, size);
        admins.getData().addAll(adminService.selectAll());
        admins.setCount(objectPage.getTotal());
        return admins;
    }

    @PostMapping("/admin/add")
    @ResponseBody
    public Object addAdmin(String username, String password,
                           Integer privileged,
                           HttpSession session) {
        Admin operator = (Admin) session.getAttribute("admin");
        if (operator != null) {
            if (username.equals("自动审核")) {
                return new Error(1008, "管理员用户名为系统保留");
            }
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(password);
            admin.setPrivileged(privileged);
            admin.setCreateTime(new Date());
            int index = adminService.addAdmin(operator, admin);
            if (index > 0) {
                return new Feedback(0, "添加管理员成功");
            } else if (index == 0) {
                return new Error(1007, "管理员用户名已存在");
            }
            return new Error(1006, "管理员权限不足");
        } else {
            return new Error(1005, "管理员身份过期，请重新登录");
        }
    }

    @PostMapping("/admin/batch/add")
    @ResponseBody
    public Object addAdmins(String[] userids,
                            Integer privileged,
                            HttpSession session) {
        int total = userids.length;
        int count = 0;
        Admin operator = (Admin) session.getAttribute("admin");
        if (operator != null) {
            if (operator.getPrivileged() == 0) {
                return new Error(1006, "管理员权限不足");
            }
            for (String userid : userids) {
                if (userid.equals("自动审核")) {
                    continue;
                }
                Admin admin = new Admin();
                admin.setUsername(userid);
                admin.setPassword(userid);
                admin.setPrivileged(privileged);
                admin.setCreateTime(new Date());
                if (adminService.existsAdmin(userid) || adminService.addAdmin(operator, admin) > 0) {
                    count++;
                }
            }
            if (count == total) {
                return new Feedback(0, "添加管理员成功");
            } else {
                return new Error(1011, "部分管理员添加失败");
            }
        } else {
            return new Error(1005, "管理员身份过期，请重新登录");
        }
    }

    @PostMapping("/admin/delete")
    @ResponseBody
    public Object deleteAdmin(Integer[] ids,
                              HttpSession session) {
        Admin operator = (Admin) session.getAttribute("admin");
        int total = ids.length;
        int count = 0;
        if (operator != null) {
            for (Integer id : ids) {
                //不能删除自己
                if (operator.getId() == id) {
                    count++;
                    continue;
                }
                if (adminService.deleteAdmin(operator, id) > 0) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == total) {
                return new Feedback(0, "删除管理员成功");
            } else {
                return new Error(1006, "管理员权限不足");
            }
        } else {
            return new Error(1005, "管理员身份过期，请重新登录");
        }
    }

    @PostMapping("/admin/password")
    @ResponseBody
    public Object deleteAdmin(String password, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin != null) {
            admin.setPassword(password);
            adminService.updateAdmin(admin, admin);
            //修改密码后需要重新登录
            session.setAttribute("admin", null);
            return new Feedback(0, "修改密码成功");
        } else {
            return new Error(1005, "管理员身份过期，请重新登录");
        }
    }

    @GetMapping("/admin/edit/{id}")
    public Object editAdminView(@PathVariable("id") Integer id, Model model) {
        Admin editInfo = adminService.selectById(id);
        model.addAttribute("editInfo", editInfo);
        return "admin-edit";
    }

    @PostMapping("/admin/edit")
    @ResponseBody
    public Object editAdmin(Integer id, String username, String password, Integer privileged, HttpSession session) {
        Admin operator = (Admin) session.getAttribute("admin");
        if (operator != null) {
            Admin admin = adminService.selectById(id);
            admin.setUsername(username);
            admin.setPassword(password);
            //不能降低自己的权限
            if (operator.getId() != admin.getId()) {
                admin.setPrivileged(privileged);
            }
            if (adminService.updateAdmin(operator, admin) >= 0) {
                return new Feedback(0, "修改管理员成功");
            } else {
                return new Error(1006, "管理员权限不足");
            }
        } else {
            return new Error(1005, "管理员身份过期，请重新登录");
        }

    }


}
