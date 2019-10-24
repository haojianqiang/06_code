package com.gpdi.web.web.config.vaccine.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.vaccine.Vaccine;
import com.gpdi.web.service.config.vaccine.VaccineService;
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
    private VaccineService vaccineService;

    @RequestMapping
    public QueryResult query(QueryData queryData, PageSettings settings){
        return vaccineService.query(queryData,settings);
    }

    @RequestMapping
    public ActionResult getVaccine(Integer vaccineId) {
        Vaccine vaccine= vaccineService.getVaccine(vaccineId);
        return ActionResult.okWithData(vaccine);
    }

    @RequestMapping
    public ActionResult save(Vaccine vaccine) {
        try {
            vaccineService.save(vaccine);
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
                vaccineService.delete(id);
            }
            return ActionResult.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }
}
