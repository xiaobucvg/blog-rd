package com.xiaobu.blog.service;

import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.exception.TokenGetException;
import com.xiaobu.blog.dto.UserOutDTO;
import com.xiaobu.blog.mapper.AdminUserMapper;
import com.xiaobu.blog.model.AdminUser;
import com.xiaobu.blog.util.MD5Util;
import com.xiaobu.blog.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
     * 获取用户信息
     */
    public Response getUserInfo(long userId) {
        AdminUser user = adminUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, "没有用户");
        }

        UserOutDTO userOutDTO = new UserOutDTO().toModel(user);

        return Response.newSuccessInstance("获取用户信息成功", userOutDTO);
    }

    /**
     * 获取 token
     */
    public Response getToken(String username, String password) {
        AdminUser adminUser = adminUserMapper._selectByUsername(username);
        if (adminUser == null) {
            throw new TokenGetException("找不到用户!");
        }
        if (!Objects.equals(adminUser.getPassword(), MD5Util.getMD5String(password))) {
            throw new TokenGetException("密码错误!");
        }
        String token = TokenUtil.getToken(adminUser.getId().toString());
        return Response.newSuccessInstance("获取成功", token);
    }
}
