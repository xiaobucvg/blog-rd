package com.xiaobu.blog.dto;

import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.dto.interfaces.Convert;
import com.xiaobu.blog.model.User;
import com.xiaobu.blog.common.FileType;
import com.xiaobu.blog.validator.annotation.MultiPartFile;
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

    @MultiPartFile(message = "头像不符合要求",required = false, type = FileType.IMAGE, maxSize = Const.FILE_IMAGE_MAX_SIZE)
    private MultipartFile avatarFile;

    private String email;

    private String phone;

    @Override
    public User copyFrom() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

}
