package com.gpdi.web.serviceimpl.monitor.warning;

import com.gpdi.web.dao.monitor.warning.PmWarningConfigDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.monitor.warning.PmWarningConfig;
import com.gpdi.web.service.monitor.warning.PmWarningConfigService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;

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

    @Override
    public void savePhones(String[] callee, String smsCont, Integer farmId) {
        //如果原来有多个号码，现在改成只有其中几个，则把原来的其他号码farm_id设置为0，方便后续删除
        generalDao.execute("update pm_warning_phone set farm_id=0 where farm_id=? and phone not in(?)",farmId, StringUtils.join(callee,","));
        for (int i = 0; i < callee.length; i++) {
            List num = generalDao.queryList(List.class,"select phone from pm_warning_phone where (farm_id=? or farm_id is null) and phone=?",farmId,callee[i]);
            if (num.size()>0){
                generalDao.execute("update pm_warning_phone set farm_id=?, introduce=? where phone=? and (farm_id=? or farm_id is null)",farmId,smsCont,callee[i],farmId);
            }else {
                generalDao.execute("delete from pm_warning_phone where phone=? and (farm_id=? or farm_id=0)",callee[i],farmId);
                generalDao.execute("insert into pm_warning_phone(phone,name,farm_id,introduce) values(?,?,?,?)",callee[i],"",farmId,smsCont);
            }
        }
    }
}
