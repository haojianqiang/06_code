package com.gpdi.web.dao;

import com.gpdi.web.entity.user.SysUser;
import pub.dao.Dao;

public interface SysUserDao extends Dao<SysUser> {

    SysUser getUser(Integer id);

}
