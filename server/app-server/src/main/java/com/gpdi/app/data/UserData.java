package com.gpdi.app.data;

import com.gpdi.app.entity.User;
import lombok.Data;

/**
 * 用户包装类
 **/
@Data
public class UserData extends User{
    private String token;
}
