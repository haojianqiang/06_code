package com.gpdi.web.daoimpl;

import com.gpdi.web.dao.SysUserDao;
import com.gpdi.web.entity.user.SysUser;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class SysUserDaoImpl extends MyBatisDao<SysUser> implements SysUserDao {
    public SysUserDaoImpl() {
        super(SysUser.class);
    }

    @Override
    public SysUser getUser(Integer id) {
        return getSqlSession().selectOne("com.gpdi.web.entity.user.SysUser.getUser",id);
    }

}
