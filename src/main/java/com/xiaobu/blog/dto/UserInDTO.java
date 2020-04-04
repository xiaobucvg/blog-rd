package com.xiaobu.blog.dto;

import com.xiaobu.blog.model.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

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
        return user;
    }

}
