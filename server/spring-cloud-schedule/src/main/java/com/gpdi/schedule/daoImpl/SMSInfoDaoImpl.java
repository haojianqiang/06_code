package com.gpdi.schedule.daoImpl;

import com.gpdi.schedule.dao.SMSInfoDao;
import com.gpdi.schedule.entity.sms.SMSInfo;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class SMSInfoDaoImpl extends MyBatisDao<SMSInfo> implements SMSInfoDao {
    public SMSInfoDaoImpl() {
        super(SMSInfo.class);
    }


}
