package com.xiaobu.blog.dto;

import com.xiaobu.blog.model.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author zh  --2020/3/23 16:15
 */
@Data
public class UserOutDTO implements Convert<User> {
    private Long id;

    private String nickname;

    private String username;

    private String avatar;

    @Override
    public UserOutDTO copyFrom(User user) {
        BeanUtils.copyProperties(user,this);
        return this;
    }
}
