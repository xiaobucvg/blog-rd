package com.xiaobu.blog.service;

import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.dto.TokenOutDTO;
import com.xiaobu.blog.dto.UserInDTO;
import com.xiaobu.blog.dto.UserOutDTO;
import com.xiaobu.blog.exception.TokenException;
import com.xiaobu.blog.exception.UserException;
import com.xiaobu.blog.mapper.UserMapper;
import com.xiaobu.blog.model.User;
import com.xiaobu.blog.model.UserExample;
import com.xiaobu.blog.util.FileUploadUtil;
import com.xiaobu.blog.util.MD5Util;
import com.xiaobu.blog.util.NetUtil;
import com.xiaobu.blog.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

/**
 * @author zh  --2020/3/23 10:17
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LogService logService;

    /**
     * 获取用户信息
     */
    public Response getUserInfo(long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new UserException("获取失败，ID 为" + userId + "的用户不存在");
        }

        UserOutDTO userOutDTO = new UserOutDTO().copyFrom(user);

        return Response.newSuccessInstance("获取用户信息成功", userOutDTO);
    }

    /**
     * 获取 token
     */
    public Response getToken(String username, String password) {
        User user = userMapper._selectByUsername(username);
        if (user == null) {
            logService.saveLog("尝试获取 token 失败，没有用户");
            throw new TokenException("没有找到用户");
        }
        try {
            if (!Objects.equals(user.getPassword(), MD5Util.getMD5String(password))) {
                logService.saveLog("尝试获取 token 失败，密码错误");
                throw new TokenException("密码输入错误");
            }
            String token = null;
            token = tokenUtil.getToken(user.getId().toString());
            TokenOutDTO tokenOutDTO = new TokenOutDTO(token, Const.TOKEN_EXP_TIME);
            logService.saveLog("获取了 token ");
            return Response.newSuccessInstance("获取成功", tokenOutDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logService.saveLog("尝试获取 token 失败，获取时发生错误");
            throw new TokenException("内部错误");
        }
    }

    /**
     * 更改用户信息
     */
    @Transactional
    public Response updateUser(UserInDTO userInDTO) {
        User user = userInDTO.copyFrom();
        // 上传头像
        if (userInDTO.getAvatarFile() != null) {
            try {
                String avatar = fileUploadUtil.uploadFile(userInDTO.getAvatarFile(), "user");
                user.setAvatar(NetUtil.getServerAddress() + "/" + avatar);
            } catch (IOException e) {
                e.printStackTrace();
                logService.saveLog("尝试修改ID: " + userInDTO.getId() + " 的用户信息失败");
                throw new UserException("修改失败，用户头像上传失败");
            }
        }
        try {
            int res = userMapper.updateByPrimaryKeySelective(user);
            if (res == 1) {
                logService.saveLog("修改 ID : " + userInDTO.getId() + " 的用户信息成功");
                return Response.newSuccessInstance("修改成功");
            } else {
                logService.saveLog("尝试修改 ID : " + userInDTO.getId() + " 的用户信息失败");
                throw new UserException("修改失败，没有用户被修改");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.saveLog("尝试修改ID: " + userInDTO.getId() + " 的用户信息失败");
            throw new UserException("修改失败，修改用户信息发生错误");
        }
    }

    /**
     * 更新用户的密码
     */
    @Transactional
    public Response updatePassword(String oldPassword, String newPassword, Long userId) {
        // 1. 检查旧密码
        UserExample example = new UserExample();
        try {
            example.createCriteria().andPasswordEqualTo(MD5Util.getMD5String(oldPassword)).andIdEqualTo(userId);
            List<User> users = userMapper.selectByExample(example);
            if (users == null || users.size() != 1) {
                logService.saveLog("尝试更新密码失败");
                throw new UserException("更新失败，旧密码输入不正确");
            }
            // 2. 更新密码
            User admin = users.get(0);
            admin.setPassword(MD5Util.getMD5String(newPassword));
            int res = userMapper.updateByPrimaryKeySelective(admin);
            if (res == 1) {
                logService.saveLog("修改密码成功");
                return Response.newSuccessInstance("更新密码成功");
            } else {
                logService.saveLog("尝试修改密码失败");
                throw new UserException("更新失败，没有用户被修改");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.saveLog("尝试修改密码失败");
            throw new UserException("更新密码发生错误");
        }
    }
}
