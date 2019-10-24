package com.gpdi.web.daoimpl.config.chicksDistribution;

import com.gpdi.web.dao.config.chicksDistribution.ChicksDistributionDao;
import com.gpdi.web.entity.config.chicksDistribution.ChicksDistribution;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;
@Repository
public class ChicksDistributionDaoImpl extends MyBatisDao<ChicksDistribution> implements ChicksDistributionDao {
    public ChicksDistributionDaoImpl() {
        super(ChicksDistribution.class);
    }
}
