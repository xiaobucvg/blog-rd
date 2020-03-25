package com.xiaobu.blog.controller.admin;

import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.model.AdminUser;
import com.xiaobu.blog.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author zh  --2020/3/23 10:13
 */
@RestController
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 登录
     */
    @PostMapping
    @RequestMapping("/login")
    public Response loginAdmin(@RequestBody Map<String, String> loginInfo, HttpSession session) {
        String username = loginInfo.get("username");
        String password = loginInfo.get("password");

        Response response = adminUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.USER, response.getData());
        }
        return response;
    }


    /**
     * 获取用户信息
     */
    @GetMapping
    @RequestMapping("/user")
    public Response getAdminInfo(HttpSession session) {
        AdminUser adminUser = (AdminUser) session.getAttribute(Const.USER);
        if (adminUser == null) {
            return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, "无法查看个人信息!");
        }
        return adminUserService.getUserInfo(adminUser.getId());
    }

    /**
     * 退出登录
     */
    @GetMapping
    @RequestMapping("/logout")
    public Response logout(HttpSession session) {
        session.removeAttribute(Const.USER);
        return Response.newSuccessInstance("退出登录成功");
    }

}
