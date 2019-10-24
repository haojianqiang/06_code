package com.gpdi.web.daoimpl;

import com.gpdi.web.dao.SysRoleUserDao;
import com.gpdi.web.entity.roleUser.SysRoleUser;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

import java.util.Map;

@Repository
public class SysRoleUserDaoImpl extends MyBatisDao<SysRoleUser> implements SysRoleUserDao {
    public SysRoleUserDaoImpl(){super(SysRoleUser.class);}

    @Override
    public void updataRoleByUser(Map map) {
        getSqlSession().update("com.gpdi.web.entity.roleUser.SysRoleUser.updataRoleByUser", map);
    }

}
