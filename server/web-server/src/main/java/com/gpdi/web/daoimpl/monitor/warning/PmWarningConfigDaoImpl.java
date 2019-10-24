package com.gpdi.web.daoimpl.monitor.warning;

import com.gpdi.web.dao.monitor.warning.PmWarningConfigDao;
import com.gpdi.web.entity.monitor.warning.PmWarningConfig;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class PmWarningConfigDaoImpl extends MyBatisDao<PmWarningConfig> implements PmWarningConfigDao {
    public PmWarningConfigDaoImpl() {
        super(PmWarningConfig.class);
    }
}
