package com.gpdi.schedule.daoImpl;

import com.gpdi.schedule.dao.SMSEchoDao;
import com.gpdi.schedule.entity.sms.SMSEcho;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class SMSEchoDaoDaoImpl extends MyBatisDao<SMSEcho> implements SMSEchoDao {
    public SMSEchoDaoDaoImpl() {
        super(SMSEcho.class);
    }


}
