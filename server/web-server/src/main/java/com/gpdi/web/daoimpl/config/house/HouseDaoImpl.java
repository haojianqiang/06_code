package com.gpdi.web.daoimpl.config.house;

import com.gpdi.web.dao.config.house.HouseDao;
import com.gpdi.web.entity.config.house.House;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class HouseDaoImpl extends MyBatisDao<House> implements HouseDao {
    public HouseDaoImpl() {
        super(House.class);
    }
}
