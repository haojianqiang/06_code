package com.gpdi.web.daoimpl;

import com.gpdi.web.dao.SysRoleModuleDao;
import com.gpdi.web.entity.roleModule.SysRoleModule;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

import java.util.Map;


@Repository
public class SysRoleModuleDaoImpl extends MyBatisDao<SysRoleModule> implements SysRoleModuleDao {

    public SysRoleModuleDaoImpl() {
        super(SysRoleModule.class);
    }

    @Override
    public SysRoleModule getByModuleId(Integer id) {
        return null;
    }

    @Override
    public void updataTree(Map map) {
        getSqlSession().update("com.gpdi.web.entity.SysRoleModule.updataTree", map);
    }
}
