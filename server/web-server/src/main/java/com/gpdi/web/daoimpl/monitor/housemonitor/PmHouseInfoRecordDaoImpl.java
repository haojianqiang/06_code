package com.gpdi.web.daoimpl.monitor.housemonitor;

import com.gpdi.web.dao.monitor.housemonitor.PmHouseInfoRecordDao;
import com.gpdi.web.entity.monitor.housemonitor.PmHouseInfoRecord;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class PmHouseInfoRecordDaoImpl extends MyBatisDao<PmHouseInfoRecord> implements PmHouseInfoRecordDao {
    public PmHouseInfoRecordDaoImpl() {
        super(PmHouseInfoRecord.class);
    }
}
