package com.gpdi.web.web.home.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.service.config.chicks.ChicksService;
import com.gpdi.web.service.home.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;
import pub.spring.web.mvc.ActionResult;

import java.util.List;
import java.util.Map;

@RestController
public class QueryAction {
    private final static Logger logger = LoggerFactory.getLogger(com.gpdi.web.web.config.house.action.QueryAction.class);

    @Autowired
    private ChicksService chicksService;

    @RequestMapping
    public Map<String, Object> earlyWarningQuery(String regionName,Integer farmId,Integer houseId){
        return queryService.getEarlyWarningQuery(regionName,farmId,houseId);
    }

    @RequestMapping
    public Map<String, Object> getDropdownBoxFarmList(String regionName){
        return queryService.getDropdownBoxFarmList(regionName);
    }

    @RequestMapping
    public Map<String, Object> getDropdownBoxHouseList(String farmId){
        return queryService.getDropdownHouseBoxList(farmId);
    }

    @Autowired
    private QueryService queryService;

    @RequestMapping
    public ActionResult getTransactionInfo(Integer houseid){
        //获取鸡的出存栏信息
        List transactions=chicksService.getTransactionInfoByHouseid(houseid);
        return ActionResult.okWithData(transactions);
    }

    @RequestMapping
    public ActionResult getProductionInfo(String startTime,String endTime){
        //获取鸡的成交信息
        List productions=chicksService.getProductionInfoByDate(startTime,endTime);
        return ActionResult.okWithData(productions);
    }
}

