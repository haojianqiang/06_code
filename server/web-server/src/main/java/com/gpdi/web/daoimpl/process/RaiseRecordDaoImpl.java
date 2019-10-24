package com.gpdi.web.daoimpl.process;

import com.gpdi.web.dao.SysUserDao;
import com.gpdi.web.dao.process.RaiseRecordDao;
import com.gpdi.web.entity.process.RaiseRecord;
import com.gpdi.web.entity.user.SysUser;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class RaiseRecordDaoImpl extends MyBatisDao<RaiseRecord> implements RaiseRecordDao {
    public RaiseRecordDaoImpl() {
        super(RaiseRecord.class);
    }
}
