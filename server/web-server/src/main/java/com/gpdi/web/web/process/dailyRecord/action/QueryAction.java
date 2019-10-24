package com.gpdi.web.web.process.dailyRecord.action;

import com.gpdi.web.dao.process.DailyRecordDao;
import com.gpdi.web.dao.process.DeathEliminationDao;
import com.gpdi.web.data.Dict;
import com.gpdi.web.data.process.dailyRecord.DailyData;
import com.gpdi.web.data.process.deathElimination.EliminationData;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.process.DailyRecord;
import com.gpdi.web.entity.process.DeathElimination;
import com.gpdi.web.entity.user.SysUser;
import com.gpdi.web.entity.vo.ChicksDisVo;
import com.gpdi.web.entity.vo.FarmTree;
import com.gpdi.web.service.SysUserService;
import com.gpdi.web.service.config.farm.FarmService;
import com.gpdi.web.service.process.DailyRecordService;
import com.gpdi.web.service.process.DeathEliminationService;
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
    private final static Logger logger = LoggerFactory.getLogger(QueryAction.class);

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RaiseRecordService raiseRecordService;
    @Autowired
    private DailyRecordService dailyRecordService;
    @Autowired
    private DailyRecordDao dailyRecordDao;

    @RequestMapping
    public QueryResult query(DailyData queryData, PageSettings settings){
        return dailyRecordService.query(queryData,settings);
    }

    @RequestMapping
    public ActionResult editHandle(Integer dailyId,Integer farmId,Integer houseId) {
        DailyRecord dailyRecord = dailyRecordDao.get(dailyId);
        List<House> listHouse = raiseRecordService.queryHouseByFarmId(farmId);
        List listChicksCcs = raiseRecordService.queryChicksCcsByHouseId(houseId).getRows();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("dailyRecord",dailyRecord);
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

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("listFarm",listFarm);
        map.put("listFarmTree",farmTreeListFarm);
        return ActionResult.okWithData(map);
    }

    @RequestMapping
    public ActionResult save(DailyRecord dailyRecord, HttpServletRequest request) {
        if(dailyRecord.getId()!= null){
            HttpSession session = request.getSession();
            int userId = (Integer)session.getAttribute("UserId");
            SysUser user = sysUserService.getUser(userId);
            dailyRecord.setModifier(user.getAccount());
            dailyRecord.setModifierId(userId);
            dailyRecord.setModifyTime(new Date());
        }else {
            dailyRecord.setCreateTime(new Date());
        }
        try {

            dailyRecordDao.save(dailyRecord);
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
                dailyRecordDao.delete(id);
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
