package com.xiaobu.blog.controller.admin;

import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.dto.UserInDTO;
import com.xiaobu.blog.service.UserService;
import com.xiaobu.blog.util.TokenUtil;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @author zh  --2020/3/23 10:13
 */
@RestController
@RequestMapping("/admin")
@Validated
public class UserController {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserService userService;

    /**
     * 获取 Token
     * /admin/token?username=""&password=""
     */
    @GetMapping("/token")
    public Response getToken(@RequestParam @NotBlank(message = "用户名不能为空") String username,
                             @RequestParam @NotBlank(message = "密码不能为空") String password) {

        return userService.getToken(username, password);
    }


    /**
     * 获取用户信息
     * /admin/user
     */
    @GetMapping("/user")
    public Response getAdminInfo(@RequestHeader("auth-token") String token) {
        Long userId = tokenUtil.getTokenSub(token);
        return userService.getUserInfo(userId);
    }

    /**
     * 登录状态下更改密码
     * /admin/user/password PUT
     */
    @PutMapping("/user/password")
    public Response putPassword(@RequestHeader("auth-token") String token,
                                @RequestParam(required = false) @NotBlank(message = "旧密码不能为空") String oldPassword,
                                @RequestParam(required = false) @Length(min = 6, max = 32, message = "密码位数最少 6 位，最长 32 位") String newPassword) {
        Long userId = tokenUtil.getTokenSub(token);
        return userService.updatePassword(oldPassword, newPassword, userId);
    }

    /**
     * 修改用户信息
     * /admin/user POST
     */
    @PostMapping("/user")
    public Response postUser(UserInDTO userInDTO, @RequestHeader(name = Const.TOKEN) String authToken) {
        Long userId = tokenUtil.getTokenSub(authToken);
        userInDTO.setId(userId);
        return userService.updateUser(userInDTO);
    }


}
