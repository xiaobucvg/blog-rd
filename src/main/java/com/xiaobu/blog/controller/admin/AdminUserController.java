package com.xiaobu.blog.controller.admin;

import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.dto.UserInDTO;
import com.xiaobu.blog.exception.AdminUserException;
import com.xiaobu.blog.service.AdminUserService;
import com.xiaobu.blog.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
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
     * 获取 Token
     * /admin/token?username=""&password=""
     */
    @GetMapping("/token")
    public Response getToken(@RequestParam String username, @RequestParam String password, HttpSession session) {
        return adminUserService.getToken(username, password);
    }

    /**
     * 获取用户信息
     * /admin/user
     */
    @GetMapping("/user")
    public Response getAdminInfo(@RequestHeader("auth-token") String token) {
        String userId = TokenUtil.getTokenSub(token);
        return adminUserService.getUserInfo(Long.parseLong(userId));
    }

    /**
     * 修改用户信息
     * /admin/user POST
     */
    @PostMapping("/user")
    public Response postUser(HttpServletRequest httpRequest) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) httpRequest;
        UserInDTO userInDTO = new UserInDTO();
        MultipartFile avatarFile = multipartRequest.getFile("avatar-file");
        userInDTO.setAvatarFile(avatarFile);
        userInDTO.setEmail(multipartRequest.getParameter("email"));
        userInDTO.setNickname(multipartRequest.getParameter("nickname"));
        userInDTO.setPhone(multipartRequest.getParameter("phone"));
        userInDTO.setUsername(multipartRequest.getParameter("username"));
        String token = multipartRequest.getHeader("auth-token");
        Long userId = Long.valueOf(TokenUtil.getTokenSub(token));
        userInDTO.setId(userId);

        return adminUserService.updateUser(userInDTO);
    }

    /**
     * 更改密码
     * /admin/user/password PUT
     */
    @PutMapping("/user/password")
    public Response putPassword(@RequestHeader("auth-token") String token, @RequestBody Map<String, String> passwordMap) {
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");
        if(newPassword.length() < 6){
            throw new AdminUserException("密码至少 6 位");
        }
        Long userId = Long.valueOf(TokenUtil.getTokenSub(token));
        return adminUserService.updatePassword(oldPassword,newPassword,userId);
    }
}
