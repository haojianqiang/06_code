package com.gpdi.web.web.saleRecord.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.data.SaleRecordCcsData;
import com.gpdi.web.data.SaleRecordData;
import com.gpdi.web.entity.config.vaccine.Vaccine;
import com.gpdi.web.entity.saleRecord.SaleRecord;
import com.gpdi.web.service.config.vaccine.VaccineService;
import com.gpdi.web.service.saleRecord.SaleRecordSevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;
import pub.spring.web.mvc.ActionResult;

import java.util.List;

@RestController
public class QueryAction {
    private final static Logger logger = LoggerFactory.getLogger(QueryAction.class);

    @Autowired
    private SaleRecordSevice saleRecordSevice;

    @RequestMapping
    public QueryResult query(SaleRecordData queryData, PageSettings settings){
        return saleRecordSevice.query(queryData,settings);
    }

    @RequestMapping
    public ActionResult getSaleRecord(Integer saleRecordId) {
        SaleRecord saleRecord = saleRecordSevice.getSaleRecord(saleRecordId);
        return ActionResult.okWithData(saleRecord);
    }

    @RequestMapping
    public ActionResult save(SaleRecord saleRecord, String saleRecordCcs) {
        try {
            saleRecordSevice.save(saleRecord,saleRecordCcs);
            return ActionResult.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult delete(Integer[] ids) {
        try {
            for(Integer id: ids) {
                saleRecordSevice.delete(id);
            }
            return ActionResult.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult queryHouseDetails(String parentId) {
        try {
            List<SaleRecordCcsData> result = saleRecordSevice.queryHouseDetails(parentId);
            return ActionResult.okWithData(result);
        }catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }
}
