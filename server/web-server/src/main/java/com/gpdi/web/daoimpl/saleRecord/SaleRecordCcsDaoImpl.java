package com.gpdi.web.daoimpl.saleRecord;

import com.gpdi.web.dao.saleRecord.SaleRecordCcsDao;
import com.gpdi.web.entity.saleRecord.SaleRecordCcs;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class SaleRecordCcsDaoImpl extends MyBatisDao<SaleRecordCcs> implements SaleRecordCcsDao {
    public SaleRecordCcsDaoImpl() {
        super(SaleRecordCcs.class);
    }
}
