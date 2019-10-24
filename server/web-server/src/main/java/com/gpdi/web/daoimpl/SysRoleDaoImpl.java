package com.gpdi.web.daoimpl;

import com.gpdi.web.dao.SysRoleDao;
import com.gpdi.web.entity.role.SysRole;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

import java.util.List;


@Repository
public class SysRoleDaoImpl extends MyBatisDao<SysRole> implements SysRoleDao {

    public SysRoleDaoImpl(){
        super(SysRole.class);
    }

    @Override
    public List<Integer> getRoleByUserId(Integer userId) {
        List<Integer> list=getSqlSession().selectList("com.gpdi.web.entity.role.SysRole.getRoleByUserId",userId);
        return list ;
    }

    @Override
    public List<Integer> getAllRoleId() {
       return getSqlSession().selectList("com.gpdi.web.entity.role.SysRole.getAllRoleId");
    }
}
