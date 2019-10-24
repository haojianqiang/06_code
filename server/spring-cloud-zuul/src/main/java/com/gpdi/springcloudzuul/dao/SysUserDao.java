package com.gpdi.springcloudzuul.dao;

import com.gpdi.springcloudzuul.entity.SysUser;
import pub.dao.Dao;

public interface SysUserDao extends Dao<SysUser>{
    // 根据账号获取用户
    SysUser getUser(String account);
}
