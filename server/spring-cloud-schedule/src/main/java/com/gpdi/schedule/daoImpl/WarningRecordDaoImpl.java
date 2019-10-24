package com.gpdi.schedule.daoImpl;

import com.gpdi.schedule.dao.WarningRecordDao;
import com.gpdi.schedule.entity.warning.WarningRecord;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class WarningRecordDaoImpl extends MyBatisDao<WarningRecord> implements WarningRecordDao {
    public WarningRecordDaoImpl() {
        super(WarningRecord.class);
    }


}
