package com.gpdi.web.daoimpl.saleRecord;

import com.gpdi.web.dao.saleRecord.SaleRecordDao;
import com.gpdi.web.entity.saleRecord.SaleRecord;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class SaleRecordDaoImpl extends MyBatisDao<SaleRecord> implements SaleRecordDao {
    public SaleRecordDaoImpl() {
        super(SaleRecord.class);
    }
}
