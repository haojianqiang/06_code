package com.gpdi.web.service.saleRecord;

import com.gpdi.web.data.SaleRecordCcsData;
import com.gpdi.web.data.SaleRecordData;
import com.gpdi.web.entity.saleRecord.SaleRecord;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.util.List;

public interface SaleRecordSevice {
    QueryResult query(SaleRecordData queryData, QuerySettings settings);

    SaleRecord getSaleRecord(Integer id);

    void save(SaleRecord saleRecord,String saleRecordCcs);

    void delete(Integer id);

    List<SaleRecordCcsData> queryHouseDetails(String farmId);
}
