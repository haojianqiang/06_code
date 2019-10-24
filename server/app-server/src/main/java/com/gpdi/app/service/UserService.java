package com.gpdi.app.service;

import com.gpdi.app.data.UserData;

public interface UserService {
    // 登陆
    UserData login(String account,String password);
}
