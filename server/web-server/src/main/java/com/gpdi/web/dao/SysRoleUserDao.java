package com.gpdi.web.dao;

import com.gpdi.web.entity.roleUser.SysRoleUser;
import pub.dao.Dao;

import java.util.Map;

public interface SysRoleUserDao extends Dao<SysRoleUser>{
    void updataRoleByUser(Map map);
}
