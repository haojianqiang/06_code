package com.gpdi.web.serviceimpl.saleRecord;

import com.gpdi.web.dao.saleRecord.SaleRecordCcsDao;
import com.gpdi.web.dao.saleRecord.SaleRecordDao;
import com.gpdi.web.data.SaleRecordCcsData;
import com.gpdi.web.data.SaleRecordData;
import com.gpdi.web.entity.monitor.warning.PmWarningConfig;
import com.gpdi.web.entity.saleRecord.SaleRecord;
import com.gpdi.web.entity.saleRecord.SaleRecordCcs;
import com.gpdi.web.service.saleRecord.SaleRecordSevice;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;
import pub.functions.StrFuncs;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class SaleRecordSeviceImpl implements SaleRecordSevice {

    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private SaleRecordDao saleRecordDao;
    @Autowired
    private SaleRecordCcsDao saleRecordCcsDao;

    @Override
    public QueryResult query(SaleRecordData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (StrFuncs.notEmpty(queryData.getStartTime())) {
            where += " and sale_time >= :startTime";
            query.put("startTime", queryData.getStartTime());
        }
        if (StrFuncs.notEmpty(queryData.getEndTime())) {
            where += " and sale_time <= :endTime";
            query.put("endTime", queryData.getEndTime());
        }
        if (StrFuncs.notEmpty(queryData.getAssemblers())) {
            where += " and assemblers like :assemblers";
            query.put("assemblers", queryData.getAssemblers());
        }
        query.select("id,assemblers,sale_time,count,total,price")
                .from("pm_sale_record")
                .where(where);
        generalDao.execute(query);
        return query.getResult();
    }

    @Override
    public SaleRecord getSaleRecord(Integer id) {
        return null;
    }

    @Override
    @Transactional
    public void save(SaleRecord saleRecord, String saleRecordCcs) {
        saleRecordDao.save(saleRecord);
        List<SaleRecordCcs> ccs = (List<SaleRecordCcs>) JSONArray.toCollection(JSONArray.fromObject(saleRecordCcs), SaleRecordCcs.class);
        for (SaleRecordCcs item : ccs){
           item.setParentId(saleRecord.getId());
            saleRecordCcsDao.save(item);
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        saleRecordDao.delete(id);
        generalDao.execute("delete from pe_sale_record_ccs where parent_id = "+id);
    }

    @Override
    public List<SaleRecordCcsData> queryHouseDetails(String farmId) {
     List<SaleRecordCcsData> list =  generalDao.queryList(SaleRecordCcsData.class, "select p.sale_count as saleCount,h.name  from pe_sale_record_ccs p , cm_house h where p.house_id = h.id and p.parent_id = " + farmId);
     return  list;
    }
}
