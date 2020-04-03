package com.xiaobu.blog.service;

import com.xiaobu.blog.aspect.annotation.Log;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.dto.UserInDTO;
import com.xiaobu.blog.dto.UserOutDTO;
import com.xiaobu.blog.exception.UserException;
import com.xiaobu.blog.exception.TokenException;
import com.xiaobu.blog.mapper.UserMapper;
import com.xiaobu.blog.model.User;
import com.xiaobu.blog.model.UserExample;
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
public class UserService {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户信息
     */
    public Response getUserInfo(long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
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
        User user = userMapper._selectByUsername(username);
        if (user == null) {
            throw new TokenException("获取 token 失败，找不到用户");
        }
        if (!Objects.equals(user.getPassword(), MD5Util.getMD5String(password))) {
            throw new TokenException("获取 token 失败，密码错误");
        }
        String token = null;
        try {
            token = tokenUtil.getToken(user.getId().toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("获取 token 时发生错误");
        }
        return Response.newSuccessInstance("获取 token 成功", token);
    }

    /**
     * 更改用户信息
     */
    @Log("更新了用户信息")
    @Transactional
    public Response updateUser(UserInDTO userInDTO) {

        User user = userInDTO.toModel();

        try {
            int res = userMapper.updateByPrimaryKeySelective(user);
            if (res == 1) {
                return Response.newSuccessInstance("更新用户信息成功");
            }
            else {
                throw new UserException("更新用户信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("更新用户信息发生错误");
        }
    }

    /**
     * 更新用户的密码
     */
    @Log("重新设置了密码")
    @Transactional
    public Response updatePassword(String oldPassword, String newPassword, Long userId) {
        // 1. 检查旧密码
        UserExample example = new UserExample();
        example.createCriteria().andPasswordEqualTo(MD5Util.getMD5String(oldPassword)).andIdEqualTo(userId);
        List<User> users = userMapper.selectByExample(example);
        if (users == null || users.size() != 1) {
            throw new UserException("旧密码输入不正确");
        }
        // 2. 更新密码
        User admin = users.get(0);
        admin.setPassword(MD5Util.getMD5String(newPassword));
        try {
            int res = userMapper.updateByPrimaryKeySelective(admin);
            if (res == 1) {
                return Response.newSuccessInstance("更新密码成功");
            } else {
                throw new UserException("更新密码失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("更新密码发生错误");
        }
    }
}
