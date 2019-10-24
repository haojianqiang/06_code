package com.gpdi.web.web.process.deathElimination.action;

import com.gpdi.web.dao.process.DeathEliminationDao;
import com.gpdi.web.data.Dict;
import com.gpdi.web.data.process.deathElimination.EliminationData;
import com.gpdi.web.data.process.feeding.FeedingData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.feed.Feed;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.process.DeathElimination;
import com.gpdi.web.entity.process.RaiseRecord;
import com.gpdi.web.entity.user.SysUser;
import com.gpdi.web.entity.vo.ChicksDisVo;
import com.gpdi.web.entity.vo.FarmTree;
import com.gpdi.web.service.SysUserService;
import com.gpdi.web.service.config.farm.FarmService;
import com.gpdi.web.service.process.DeathEliminationService;
import com.gpdi.web.service.process.RaiseRecordService;
import com.gpdi.web.utils.DateUtils;
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
    private final static Logger logger = LoggerFactory.getLogger(QueryAction.class);

    @Autowired
    private DeathEliminationService deathEliminationService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private DeathEliminationDao deathEliminationDao;
    @Autowired
    private RaiseRecordService raiseRecordService;

    @RequestMapping
    public QueryResult query(EliminationData queryData, PageSettings settings){
        return deathEliminationService.query(queryData,settings);
    }

    @RequestMapping
    public ActionResult editHandle(Integer eliminationId,Integer farmId,Integer houseId) {
        DeathElimination deathElimination = deathEliminationDao.get(eliminationId);
        List<House> listHouse = raiseRecordService.queryHouseByFarmId(farmId);
        List listChicksCcs = raiseRecordService.queryChicksCcsByHouseId(houseId).getRows();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("deathElimination",deathElimination);
        map.put("listHouse",listHouse);
        map.put("listChicksCcs",listChicksCcs);
        return ActionResult.okWithData(map);
    }

    @RequestMapping
    public ActionResult init() {
        List<FarmTree> farmTreeListFarm = new ArrayList<>();
        List<Farm> listFarm = raiseRecordService.quertAllFarm();
        for(Farm farm : listFarm){
            FarmTree farmTreeFarm = new FarmTree();
            List<FarmTree> farmTreeListHouse = new ArrayList<>();
            farmTreeFarm.setId(farm.getId());
            farmTreeFarm.setName(farm.getName());
            farmTreeFarm.setDisabled(true);
            List<House> listHouse = raiseRecordService.queryHouseByFarmId(farm.getId());
            for(House house : listHouse){
                FarmTree farmTreeHouse = new FarmTree();
                farmTreeHouse.setId(house.getId());
                farmTreeHouse.setName(house.getName());
                farmTreeHouse.setDisabled(true);
                List<FarmTree> farmTreeListChicksDis = new ArrayList<>();
                List<ChicksDisVo> chicksDisVoList = raiseRecordService.getChicksDisIdWithBatchNum(house.getId());
                for(ChicksDisVo vo : chicksDisVoList){
                    FarmTree farmTreeChicksDis = new FarmTree();
                    farmTreeChicksDis.setId(vo.getId());
                    farmTreeChicksDis.setName(vo.getBatchNumber());
                    farmTreeListChicksDis.add(farmTreeChicksDis);
                }
                farmTreeHouse.setChildren(farmTreeListChicksDis);
                farmTreeListHouse.add(farmTreeHouse);
            }
            farmTreeFarm.setChildren(farmTreeListHouse);
            farmTreeListFarm.add(farmTreeFarm);
        }

        List<Dict> listType = raiseRecordService.queryDict("pm_death_elimination_record.type");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("listFarm",listFarm);
        map.put("listType",listType);
        map.put("listFarmTree",farmTreeListFarm);
        return ActionResult.okWithData(map);
    }

    @RequestMapping
    public ActionResult save(DeathElimination deathElimination, HttpServletRequest request) {
        if(deathElimination.getId()!= null){
            HttpSession session = request.getSession();
            int userId = (Integer)session.getAttribute("UserId");
            SysUser user = sysUserService.getUser(userId);
            deathElimination.setModifier(user.getAccount());
            deathElimination.setModifierId(userId);
            deathElimination.setModifyTime(new Date());
        }else {
            deathElimination.setCreateTime(new Date());
        }
        try {

            deathEliminationDao.save(deathElimination);
            return ActionResult.ok();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult delete(Integer[] ids) {
        try {
            for(Integer id: ids) {
                deathEliminationDao.delete(id);
            }
            return ActionResult.ok();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult queryHouse(Integer farmId) {
        List<House> listHouse = raiseRecordService.queryHouseByFarmId(farmId);
        return ActionResult.okWithData(listHouse);
    }

    @RequestMapping
    public QueryResult queryChicksCcs(Integer houseId){
        return raiseRecordService.queryChicksCcsByHouseId(houseId);
    }

}
