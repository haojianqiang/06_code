package com.gpdi.web.web.monitor.housemonitor.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.monitor.housemonitor.PmHouseInfoRecord;
import com.gpdi.web.entity.vo.ChicksDisVo;
import com.gpdi.web.service.monitor.housemonitor.PmHouseInfoRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;
import pub.spring.web.mvc.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
public class QueryAction {
    private final static Logger logger = LoggerFactory.getLogger(QueryAction.class);

    @Autowired
    private PmHouseInfoRecordService pmHouseInfoRecordService;

    @RequestMapping
    public QueryResult query(QueryData queryData, PageSettings settings) {
        return pmHouseInfoRecordService.query(queryData, settings);
    }

    @RequestMapping
    public ActionResult getPmHouseInfoRecord(Integer id) {
        PmHouseInfoRecord pmHouseInfoRecord = pmHouseInfoRecordService.getPmHouseInfoRecord(id);
        return ActionResult.okWithData(pmHouseInfoRecord);
    }

    @RequestMapping
    public ActionResult save(PmHouseInfoRecord pmHouseInfoRecord, HttpServletRequest request) {
        if (pmHouseInfoRecord.getId() != null) {
            HttpSession session = request.getSession();
            int userId = (Integer) session.getAttribute("UserId");
            pmHouseInfoRecord.setModifierId(userId);
            pmHouseInfoRecord.setModifyTime(new Date());
        } else {
            pmHouseInfoRecord.setCreateTime(new Date());
        }
        try {
            pmHouseInfoRecordService.save(pmHouseInfoRecord);
            return ActionResult.ok();
        } catch (Exception e) {
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult delete(Integer[] ids) {
        try {
            for (Integer id : ids) {
                pmHouseInfoRecordService.delete(id);
            }
            return ActionResult.ok();
        } catch (Exception e) {
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult getFarmNameAndId(){
        List<Farm> farms = pmHouseInfoRecordService.getFarmNameAndId();
        return ActionResult.okWithData(farms);
    }

    @RequestMapping
    public ActionResult getHouseNameAndId(Integer farmId){
        List<House> houses = pmHouseInfoRecordService.getHouseNameAndId(farmId);
        return ActionResult.okWithData(houses);
    }

    @RequestMapping
    public ActionResult getBatchNumberAndId(Integer houseId){
        List<ChicksDisVo> chicksDistributions = pmHouseInfoRecordService.getBatchNumberAndId(houseId);
        return ActionResult.okWithData(chicksDistributions);
    }

}
