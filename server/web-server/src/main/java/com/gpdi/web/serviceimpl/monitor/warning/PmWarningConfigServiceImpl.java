package com.gpdi.web.serviceimpl.monitor.warning;

import com.gpdi.web.dao.monitor.warning.PmWarningConfigDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.monitor.warning.PmWarningConfig;
import com.gpdi.web.service.monitor.warning.PmWarningConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class PmWarningConfigServiceImpl implements PmWarningConfigService {

    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private PmWarningConfigDao pmWarningConfigDao;


    @Override
    public List<PmWarningConfig> query(QueryData queryData) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM  pm_warning_config  where farm_id ="+queryData.getFarmId());
        if ( queryData.getHouseId() != null){
            builder.append(" and house_id = "+queryData.getHouseId());
        }
        return generalDao.queryList(PmWarningConfig.class, builder.toString());
    }

    @Override
    public void save(PmWarningConfig pmWarningConfig) {
        //保存预警阈值，先查看该鸡棚记录是否已经存在，存在则更新，不存在则新增
        if( pmWarningConfig.getHouseId() != null && pmWarningConfig.getId() == null){
            QueryData queryData = new QueryData();
            queryData.setFarmId(pmWarningConfig.getFarmId());
            queryData.setHouseId(pmWarningConfig.getHouseId());
            List<PmWarningConfig> oldConfigs = this.query(queryData);

        }
        pmWarningConfigDao.save(pmWarningConfig);
    }
}
