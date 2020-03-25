package com.xiaobu.blog.dto;

import com.xiaobu.blog.model.AdminUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author zh  --2020/3/23 16:15
 */
@Data
public class UserOutDTO implements Convert<AdminUser> {
    private Long id;

    private String nickname;

    private String username;

    private String avatar;

    @Override
    public UserOutDTO toModel(AdminUser adminUser) {
        BeanUtils.copyProperties(adminUser,this);
        return this;
    }
}
