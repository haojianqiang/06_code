package com.gpdi.web.dao;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.module.SysModule;
import pub.dao.Dao;

import java.util.List;

/**
 *
 */
public interface SysModuleDao extends Dao<SysModule> {

    List<SysModule> getParentByRole(Integer parentId);

    List<SysModule> getParent();

    List<SysModule> getChild(Integer id);

    List<SysModule> getRoleMoudleTree(Integer id);

    List<SysModule> getUsedMoudleByRole(Integer id);

    List<String>  getMoudleByRoleAndParent(QueryData queryData);


}

