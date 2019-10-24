package com.gpdi.web.dao;

import com.gpdi.web.entity.role.SysRole;
import pub.dao.Dao;

import java.util.List;

/**
 *
 */
public interface SysRoleDao extends Dao<SysRole> {

    List<Integer> getRoleByUserId(Integer userId);

    List<Integer> getAllRoleId();
}

