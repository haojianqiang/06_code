package com.gpdi.springcloudzuul.daoImpl;

import com.gpdi.springcloudzuul.dao.SysUserDao;
import com.gpdi.springcloudzuul.entity.SysUser;
import org.springframework.stereotype.Repository;
import pub.dao.Dao;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class SysUserDaoImpl extends MyBatisDao<SysUser> implements SysUserDao{
    public SysUserDaoImpl() {
        super(SysUser.class);
    }

    @Override
    public SysUser getUser(String account) {
        return getSqlSession().selectOne("com.gpdi.springcloudzuul.entity.SysUser.getUser",account);
    }
}
