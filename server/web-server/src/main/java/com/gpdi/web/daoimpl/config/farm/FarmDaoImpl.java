package com.gpdi.web.daoimpl.config.farm;

import com.gpdi.web.dao.config.farm.FarmDao;
import com.gpdi.web.entity.config.farm.Farm;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class FarmDaoImpl extends MyBatisDao<Farm> implements FarmDao {
    public FarmDaoImpl() {
        super(Farm.class);
    }

}
