package com.xiaobu.blog.dto;

import com.xiaobu.blog.model.AdminUser;
import com.xiaobu.blog.util.FileUtil;
import com.xiaobu.blog.util.InetUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zh  --2020/3/29 9:49
 */
@Data
public class UserInDTO implements Convert<AdminUser> {

    private Long id;

    private String username;

    private String nickname;

    private MultipartFile avatarFile;

    private String email;

    private String phone;

    @Override
    public AdminUser toModel() {
        AdminUser user = new AdminUser();
        BeanUtils.copyProperties(this, user);

        // 创建存放目录
        Path dir = FileUtil.getDirectory("static", "user");

        if (this.getAvatarFile() != null) {
            // 创建头像文件
            Path avatar = FileUtil.getFile("avatar.jpg", dir);

            // 写入数据
            FileUtil.write(avatar, this.getAvatarFile());

            user.setAvatar("http://" + InetUtil.getLocalHost() + "/user/avatar.jpg");
        }

        return user;
    }

}
