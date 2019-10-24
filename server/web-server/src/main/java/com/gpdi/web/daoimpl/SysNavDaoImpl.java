package com.gpdi.web.daoimpl;

import com.gpdi.web.dao.SysNavDao;
import com.gpdi.web.entity.nav.SysNav;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class SysNavDaoImpl extends MyBatisDao<SysNav> implements SysNavDao {
    public SysNavDaoImpl() {
        super(SysNav.class);
    }
}
