package com.gpdi.web.web.config.medicine.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.medicine.Medicine;
import com.gpdi.web.service.config.medicine.MedicineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;
import pub.spring.web.mvc.ActionResult;

import java.util.Date;

@RestController
public class QueryAction {
    private final static Logger logger = LoggerFactory.getLogger(QueryAction.class);

    @Autowired
    private MedicineService medicineService;

    @RequestMapping
    public QueryResult query(QueryData queryData, PageSettings settings){
        return medicineService.query(queryData,settings);
    }

    @RequestMapping
    public ActionResult getMedicine(Integer medicineId) {
        Medicine medicine= medicineService.getMedicine(medicineId);
        return ActionResult.okWithData(medicine);
    }

    @RequestMapping
    public ActionResult save(Medicine medicine) {
        try {
            medicineService.save(medicine);
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
                medicineService.delete(id);
            }
            return ActionResult.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }
}
