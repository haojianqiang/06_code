package com.gpdi.web.daoimpl;

import com.gpdi.web.dao.SysModuleDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.module.SysModule;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

import java.util.List;


@Repository
public class SysModuleDaoImpl extends MyBatisDao<SysModule> implements SysModuleDao {

    public SysModuleDaoImpl() {
        super(SysModule.class);
    }

    @Override
    public List<SysModule> getParent() {
        return getSqlSession().selectList("com.gpdi.web.entity.module.SysModule.getParent");
    }

    @Override
    public List<SysModule> getParentByRole(Integer parentId) {
        return getSqlSession().selectList("com.gpdi.web.entity.entity.module.SysModule.getParentByRole", parentId);
    }

    @Override
    public List<SysModule> getChild(Integer id) {
        return null;
    }

    @Override
    public List<SysModule> getRoleMoudleTree(Integer id) {
        return getSqlSession().selectList("com.gpdi.web.entity.module.SysModule.getRoleMoudleTree", id);
    }

    @Override
    public List<SysModule> getUsedMoudleByRole(Integer id) {
        return getSqlSession().selectList("com.gpdi.web.entity.module.SysModule.getMoudleByRole", id);

    }

    @Override
    public List<String> getMoudleByRoleAndParent(QueryData queryData) {
        return getSqlSession().selectList("com.gpdi.web.entity.module.SysModule.getMoudleByRoleAndParent", queryData);

    }
}
