package com.gpdi.web.daoimpl.process;

import com.gpdi.web.dao.process.DailyRecordDao;
import com.gpdi.web.dao.process.DeathEliminationDao;
import com.gpdi.web.entity.process.DailyRecord;
import com.gpdi.web.entity.process.DeathElimination;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class DailyRecordDaoImpl extends MyBatisDao<DailyRecord> implements DailyRecordDao {
    public DailyRecordDaoImpl() {
        super(DailyRecord.class);
    }
}
