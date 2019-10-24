package com.gpdi.web.web.process.feeding.action;

import com.gpdi.web.data.Dict;
import com.gpdi.web.data.process.feeding.FeedingData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.feed.Feed;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.process.RaiseRecord;
import com.gpdi.web.entity.user.SysUser;
import com.gpdi.web.entity.vo.ChicksDisVo;
import com.gpdi.web.entity.vo.FarmTree;
import com.gpdi.web.service.SysUserService;
import com.gpdi.web.service.config.farm.FarmService;
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
    private FarmService farmService;
    @Autowired
    private RaiseRecordService raiseRecordService;
    @Autowired
    private SysUserService sysUserService;

    @RequestMapping
    public QueryResult query(FeedingData queryData, PageSettings settings){
        return raiseRecordService.queryFeed(queryData,settings);
    }

    @RequestMapping
    public ActionResult editHandle(Integer raiseRecordId,Integer farmId,Integer houseId) {
        RaiseRecord raiseRecord = raiseRecordService.getRaiseRecord(raiseRecordId);
        List<House> listHouse = raiseRecordService.queryHouseByFarmId(farmId);
        List listChicksCcs = raiseRecordService.queryChicksCcsByHouseId(houseId).getRows();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("raiseRecord",raiseRecord);
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
        List<Feed> listFeed = raiseRecordService.queryAllFeed();
        List<Dict> listRaiseType = raiseRecordService.queryDict("pm_raise_record.raise_type");
        Iterator typeIterator = listRaiseType.iterator();
        while(typeIterator.hasNext()){
            Dict dict = (Dict)typeIterator.next();
            if (dict.getCode() != 0) {
                typeIterator.remove();
            }
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("listFarm",listFarm);
        map.put("listFeed",listFeed);
        map.put("listRaiseType",listRaiseType);
        map.put("listFarmTree",farmTreeListFarm);
        return ActionResult.okWithData(map);
    }

    /*@RequestMapping
    public ActionResult init() {
        List<Farm> listFarm = raiseRecordService.quertAllFarm();
        List<Feed> listFeed = raiseRecordService.queryAllFeed();
        List<Dict> listRaiseType = raiseRecordService.queryDict("pm_raise_record.raise_type");
        Iterator typeIterator = listRaiseType.iterator();
        while(typeIterator.hasNext()){
            Dict dict = (Dict)typeIterator.next();
            if (dict.getCode() != 0) {
                typeIterator.remove();
            }
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("listFarm",listFarm);
        map.put("listFeed",listFeed);
        map.put("listRaiseType",listRaiseType);
        return ActionResult.okWithData(map);
    }*/

    @RequestMapping
    public ActionResult save(RaiseRecord raiseRecord, HttpServletRequest request) {
        //计算鸡苗生长周期
        Chicks chicks = raiseRecordService.queryChicksByChicksCcsId(raiseRecord.getChicksCcsId());
        int differentDays = DateUtils.differentDays(chicks.getProduceTime(),raiseRecord.getRaiseTime());
        raiseRecord.setChickDayAge(differentDays);

        if(raiseRecord.getId()!= null){
            HttpSession session = request.getSession();
            int userId = (Integer)session.getAttribute("UserId");
            SysUser user = sysUserService.getUser(userId);
            raiseRecord.setModifier(user.getAccount());
            raiseRecord.setModifierId(userId);
            raiseRecord.setModifyTime(new Date());
        }else {
            raiseRecord.setCreateTime(new Date());
        }
        try {

            raiseRecordService.save(raiseRecord);
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
                raiseRecordService.delete(id);
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
