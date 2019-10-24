package com.gpdi.web.web.config.house.action;


import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.chicksDistribution.ChicksDistribution;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.service.config.chicksDistribution.ChicksDistributionService;
import com.gpdi.web.service.config.house.HouseService;
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
    private final static Logger logger =
            LoggerFactory.getLogger(com.gpdi.web.web.config.house.action.QueryAction.class);

    @Autowired
    private ChicksDistributionService chicksDistributionService;

    @Autowired
    private HouseService houseService;

    @RequestMapping
    public QueryResult query(QueryData queryData, PageSettings settings) {
        return houseService.query(queryData, settings);
    }

    @RequestMapping
    public ActionResult getHouse(Integer houseId) {
        House house = houseService.getHouse(houseId);
        return ActionResult.okWithData(house);
    }

    @RequestMapping
    public ActionResult save(House house, HttpServletRequest request) {
        if (house.getId() != null) {
            HttpSession session = request.getSession();
            int userId = (Integer) session.getAttribute("UserId");
            house.setModifierId(userId);
            house.setModifyTime(new Date());
        } else {
            house.setCreateTime(new Date());
        }
        try {
            houseService.save(house);
            return ActionResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult delete(Integer[] ids) {
        try {
            for (Integer id : ids) {
                //删除鸡棚前判断此鸡棚是否已经分配鸡苗
                List<ChicksDistribution> chicksList = chicksDistributionService.getChick_countByHouseid(id);
                if (chicksList.size() == 0) {

                    houseService.delete(id);
                } else {
                    return ActionResult.error("删除失败，此鸡棚已分配鸡苗");
                }
            }
            return ActionResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult getFarmNameAndId() {
        List<Farm> farms = houseService.getFarmNameAndId();
        return ActionResult.okWithData(farms);
    }

}
