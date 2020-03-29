package com.xiaobu.blog.service;

import com.xiaobu.blog.aspect.annotation.Log;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.dto.UserInDTO;
import com.xiaobu.blog.exception.AdminUserException;
import com.xiaobu.blog.exception.TokenGetException;
import com.xiaobu.blog.dto.UserOutDTO;
import com.xiaobu.blog.mapper.AdminUserMapper;
import com.xiaobu.blog.model.AdminUser;
import com.xiaobu.blog.model.AdminUserExample;
import com.xiaobu.blog.util.MD5Util;
import com.xiaobu.blog.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
    @Log("获取了 Token")
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

    /**
     * 更改用户信息
     */
    @Log("更新了用户信息")
    @Transactional
    public Response updateUser(UserInDTO userInDTO) {

        AdminUser adminUser = userInDTO.toModel();

        int res = adminUserMapper.updateByPrimaryKeySelective(adminUser);

        if (res == 1) {
            return Response.newSuccessInstance("更新用户信息成功");
        }
        throw new AdminUserException("更新用户信息出现异常");
    }

    /**
     * 更新用户的密码
     */
    @Log("重新设置了密码")
    @Transactional
    public Response updatePassword(String oldPassword, String newPassword, Long userId) {
        // 1. 检查旧密码
        AdminUserExample example = new AdminUserExample();
        example.createCriteria().andPasswordEqualTo(MD5Util.getMD5String(oldPassword)).andIdEqualTo(userId);
        List<AdminUser> adminUsers = adminUserMapper.selectByExample(example);
        if (adminUsers == null || adminUsers.size() != 1) {
            throw new AdminUserException("旧密码输入不正确");
        }
        // 2. 更新密码
        AdminUser admin = adminUsers.get(0);
        admin.setPassword(MD5Util.getMD5String(newPassword));
        int res = adminUserMapper.updateByPrimaryKeySelective(admin);
        if(res != 1){
            throw new AdminUserException("更新密码失败");
        }
        return Response.newSuccessInstance("更新密码成功");
    }
}
