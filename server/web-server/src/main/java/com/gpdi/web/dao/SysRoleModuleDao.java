package com.gpdi.web.dao;

import com.gpdi.web.entity.roleModule.SysRoleModule;
import pub.dao.Dao;

import java.util.Map;

/**
 * Created by yanqi on 2016/8/26.
 */
public interface SysRoleModuleDao extends Dao<SysRoleModule> {

    SysRoleModule getByModuleId(Integer id);

    void updataTree(Map map);

}
