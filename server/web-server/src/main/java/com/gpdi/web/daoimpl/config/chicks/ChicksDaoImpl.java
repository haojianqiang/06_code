package com.gpdi.web.daoimpl.config.chicks;

import com.gpdi.web.dao.config.chicks.ChicksDao;
import com.gpdi.web.entity.config.chicks.Chicks;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class ChicksDaoImpl extends MyBatisDao<Chicks> implements ChicksDao {
    public ChicksDaoImpl() {
        super(Chicks.class);
    }
}
