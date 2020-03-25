package com.xiaobu.blog.service;

import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.common.exception.AdminUserException;
import com.xiaobu.blog.dto.UserOutDTO;
import com.xiaobu.blog.mapper.AdminUserMapper;
import com.xiaobu.blog.model.AdminUser;
import com.xiaobu.blog.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author zh  --2020/3/23 10:17
 */
@Service
@Slf4j
public class AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    /**
     * 登录
     */
    public Response login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, "用户名和密码不能为空!", null);
        }

        AdminUser adminUser = adminUserMapper._selectByUsername(username);
        if (adminUser == null) {
            return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, "登录的用户不存在!");
        }
        if (!Objects.equals(adminUser.getPassword(), MD5Util.getMD5String(password))) {
            return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, "密码错误!");
        }
        adminUser.setPassword("");
        log.info("登录成功");
        return Response.newSuccessInstance("登录成功", adminUser);
    }

    /**
     * 获取用户信息
     */
    public Response getUserInfo(long userid) {
        AdminUser user = adminUserMapper.selectByPrimaryKey(userid);
        if (user == null) {
            return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST,"没有用户");
        }

        UserOutDTO userOutDTO = new UserOutDTO().toModel(user);

        return Response.newSuccessInstance("获取用户信息成功",userOutDTO);
    }
}
