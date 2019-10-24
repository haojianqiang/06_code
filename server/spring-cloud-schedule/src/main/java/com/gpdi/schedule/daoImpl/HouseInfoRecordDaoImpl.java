package com.gpdi.schedule.daoImpl;

import com.gpdi.schedule.dao.HouseInfoRecordDao;
import com.gpdi.schedule.entity.house.HouseInfoRecord;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class HouseInfoRecordDaoImpl extends MyBatisDao<HouseInfoRecord> implements HouseInfoRecordDao {
    public HouseInfoRecordDaoImpl() {
        super(HouseInfoRecord.class);
    }
}
