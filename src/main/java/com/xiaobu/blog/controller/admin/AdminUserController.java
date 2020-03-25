package com.xiaobu.blog.controller.admin;

import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.service.AdminUserService;
import com.xiaobu.blog.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zh  --2020/3/23 10:13
 */
@RestController
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 获取 Token
     */
    @GetMapping("/token")
    public Response getToken(@RequestParam String username, @RequestParam String password, HttpSession session) {
        return adminUserService.getToken(username, password);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/user")
    public Response getAdminInfo(HttpServletRequest request) {
        // 有拦截器，不会空指针
        String token = request.getHeader(Const.TOKEN);
        String userId = TokenUtil.getTokenSub(token);
        return adminUserService.getUserInfo(Long.parseLong(userId));
    }

}
