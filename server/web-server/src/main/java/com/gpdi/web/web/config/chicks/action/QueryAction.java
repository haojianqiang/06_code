package com.gpdi.web.web.config.chicks.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.vo.FarmTree;
import com.gpdi.web.service.config.chicks.ChicksService;
import com.gpdi.web.service.process.RaiseRecordService;
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
import java.util.*;

@RestController
public class QueryAction {
    private final static Logger logger = LoggerFactory.getLogger(com.gpdi.web.web.config.house.action.QueryAction.class);

    @Autowired
    private ChicksService chicksService;
    @Autowired
    private RaiseRecordService raiseRecordService;

    @RequestMapping
    public QueryResult query(QueryData queryData, PageSettings settings) {
        return chicksService.query(queryData, settings);
    }

    @RequestMapping
    public ActionResult init() {
        List<FarmTree> farmTreeListFarm = new ArrayList<>();
        List<Farm> listFarm = raiseRecordService.quertAllFarm();
        for(Farm farm : listFarm){
            FarmTree farmTreeFarm = new FarmTree();
            farmTreeFarm.setId(farm.getId());
            farmTreeFarm.setName(farm.getName());
            farmTreeListFarm.add(farmTreeFarm);
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("listFarm",listFarm);
        map.put("listFarmTree",farmTreeListFarm);
        return ActionResult.okWithData(map);
    }

    @RequestMapping
    public ActionResult getChicks(Integer chicksId) {
        Chicks chicks = chicksService.getChicks(chicksId);
        return ActionResult.okWithData(chicks);
    }

    @RequestMapping
    public ActionResult save(Chicks chicks, HttpServletRequest request) {
        if (chicks.getId() != null) {
            HttpSession session = request.getSession();
            int userId = (Integer) session.getAttribute("UserId");
            chicks.setModifierId(userId);
            chicks.setModifyTime(new Date());
        } else {
            chicks.setCreateTime(new Date());
        }
        try {
            chicksService.save(chicks);
            return ActionResult.ok();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult delete(Integer[] ids) {
        try {
            for (Integer id : ids) {
                chicksService.delete(id);
            }
            return ActionResult.ok();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult getFarmNameAndId(){
        List<Farm> farms = chicksService.getFarmNameAndId();
        return ActionResult.okWithData(farms);
    }

}

