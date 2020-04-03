package com.xiaobu.blog.dto;

import com.xiaobu.blog.exception.UserException;
import com.xiaobu.blog.model.User;
import com.xiaobu.blog.util.FileUtil;
import com.xiaobu.blog.util.InetUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.SocketException;
import java.nio.file.Path;

/**
 * @author zh  --2020/3/29 9:49
 */
@Data
public class UserInDTO implements Convert<User> {

    private Long id;

    private String username;

    private String nickname;

    private MultipartFile avatarFile;

    private String email;

    private String phone;

    @Override
    public User toModel() {
        User user = new User();
        BeanUtils.copyProperties(this, user);

        // 创建存放目录
        Path dir = FileUtil.getDirectory("static", "user");

        if (this.getAvatarFile() != null) {
            // 创建头像文件
            Path avatar = FileUtil.getFile("avatar.jpg", dir);

            // 写入数据
            FileUtil.write(avatar, this.getAvatarFile());

            try {
                user.setAvatar("http://" + InetUtil.getRealIp() + "/user/avatar.jpg");
            } catch (SocketException e) {
                e.printStackTrace();
                throw new UserException("存储头像失败");
            }
        }

        return user;
    }

}
