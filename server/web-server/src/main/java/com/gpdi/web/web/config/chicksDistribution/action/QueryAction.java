package com.gpdi.web.web.config.chicksDistribution.action;

import com.gpdi.web.data.ChicksDistributionData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.chicksDistribution.ChicksDistribution;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.user.SysUser;
import com.gpdi.web.entity.vo.ChicksDisVo;
import com.gpdi.web.entity.vo.FarmTree;
import com.gpdi.web.entity.vo.HouseVo;
import com.gpdi.web.service.SysUserService;
import com.gpdi.web.service.config.chicks.ChicksService;
import com.gpdi.web.service.config.chicksDistribution.ChicksDistributionService;
import com.gpdi.web.service.process.RaiseRecordService;
import net.sf.json.JSONArray;
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
import java.text.ParseException;
import java.util.*;

@RestController
public class QueryAction {
    private final static Logger logger = LoggerFactory.getLogger(QueryAction.class);


    @Autowired
    private ChicksDistributionService chicksDistributionService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ChicksService chicksService;
    @Autowired
    private RaiseRecordService raiseRecordService;

    @RequestMapping
    public QueryResult query(ChicksDistributionData queryData, PageSettings settings){
        return chicksDistributionService.query(queryData,settings);
    }

    @RequestMapping
    public ActionResult save(String houseVoList, Integer chicksId, HttpServletRequest request) throws ParseException {
        JSONArray json = JSONArray.fromObject(houseVoList);
        List<HouseVo> houseVos = (List<HouseVo>) JSONArray.toCollection(json, HouseVo.class);
        for (HouseVo vo : houseVos) {
            ChicksDistribution chicksDistribution = new ChicksDistribution();
            HttpSession session = request.getSession();
            int userId = (Integer) session.getAttribute("UserId");
            SysUser user = sysUserService.getUser(userId);
            chicksDistribution.setModifier(user.getAccount());
            chicksDistribution.setModifierId(userId);
            chicksDistribution.setCreateTime(new Date());
            chicksDistribution.setHouseId(vo.getId());
            chicksDistribution.setChicksId(chicksId);
            chicksDistribution.setChickCount(vo.getChickCount());
            chicksDistribution.setOriginCode(String.format("%09d", chicksDistribution.getHouseId())+String.format("%09d", chicksDistribution.getChicksId()));
            //存栏
            chicksDistribution.setStatus(1);
            chicksDistributionService.save(chicksDistribution);
            //更新未分配数量
            Chicks chicks = chicksService.getChicks(chicksDistribution.getChicksId());
            int unallocatedCapacity = 0;
            unallocatedCapacity = chicks.getUnallocatedCapacity() - chicksDistribution.getChickCount();
            chicks.setUnallocatedCapacity(unallocatedCapacity);
            chicksService.save(chicks);
        }
        return ActionResult.ok();
    }

    @RequestMapping
    public ActionResult update(String houseVoList) throws ParseException {
        JSONArray json = JSONArray.fromObject(houseVoList);
        List<HouseVo> houseVos = (List<HouseVo>) JSONArray.toCollection(json, HouseVo.class);
        for (HouseVo vo : houseVos) {
            ChicksDistribution chicksDistribution = chicksDistributionService.getChicksDistribution(vo.getChicksDistributionId());
            //更新未分配数量
            Chicks chicks = chicksService.getChicks(chicksDistribution.getChicksId());
            int unallocatedCapacity = 0;
            unallocatedCapacity = chicks.getUnallocatedCapacity() + chicksDistribution.getChickCount() - vo.getChickCount();
            chicks.setUnallocatedCapacity(unallocatedCapacity);
            chicksService.save(chicks);
            chicksDistribution.setModifyTime(new Date());
            //更新鸡苗分配数量
            chicksDistribution.setChickCount(vo.getChickCount());
            chicksDistributionService.save(chicksDistribution);
        }
        return ActionResult.ok();
    }

    @RequestMapping
    public ActionResult delete(Integer[] ids) {
        try {
            for(Integer id: ids) {
                chicksDistributionService.delete(id);
            }
            return ActionResult.ok();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
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
    public ActionResult editHandle(Integer chicksDistributionId,Integer farmId) {
        HouseVo houseVo= chicksDistributionService.getDistributionHouse(chicksDistributionId);
        List<Chicks> chicksList = chicksDistributionService.getChicksBatchNumAndId(farmId);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("houseVo",houseVo);
        map.put("chicksList",chicksList);
        return ActionResult.okWithData(map);
    }

    @RequestMapping
    public ActionResult getFarmNameAndId(){
        List<Farm> farms = chicksDistributionService.getFarmNameAndId();
        return ActionResult.okWithData(farms);
    }

    @RequestMapping
    public ActionResult getBatchNumberByHouseId(Integer houseId){
        List<Chicks> chicks = chicksDistributionService.getChicksBatchNumByHouseId(houseId);
        return ActionResult.okWithData(chicks);
    }

    @RequestMapping
    public ActionResult getHouseAndChicks(Integer farmId){
        List<House> houseList = chicksDistributionService.getHouseNameAndId(farmId);
        List<Chicks> chicksList = chicksDistributionService.getChicksBatchNumAndId(farmId);
        Map<String,Object> map = new HashMap<>();
        map.put("houseList",houseList);
        map.put("chicksList",chicksList);
        return ActionResult.okWithData(map);
    }

    @RequestMapping
    public ActionResult getHouseNameAndId(Integer farmId){
        List<House> houseList = chicksDistributionService.getHouseNameAndId(farmId);
        return ActionResult.okWithData(houseList);
    }

    @RequestMapping
    public ActionResult getChicksBatchNumAndId(Integer farmId){
        List<Chicks> houseList = chicksDistributionService.getChicksBatchNumAndId(farmId);
        return ActionResult.okWithData(houseList);
    }

    @RequestMapping
    public ActionResult getChicksBatchNumAndTypeAndProducerId(){
        List<Chicks> chicksList = chicksDistributionService.getChicksBatchNumAndTypeAndProducerAndId();
        return ActionResult.okWithData(chicksList);
    }

    @RequestMapping
    public ActionResult getChicks(Integer chicksId){
        Chicks chicks = chicksService.getChicks(chicksId);
        return ActionResult.okWithData(chicks);
    }

    @RequestMapping
    public ActionResult getUnDistributionHouse(Integer farmId){
        List<HouseVo> houseList = chicksDistributionService.getUnDistributionHouse(farmId);
        List<Chicks> chicksList = chicksDistributionService.getChicksBatchNumAndId(farmId);
        Map<String,Object> map = new HashMap<>();
        map.put("houseList",houseList);
        map.put("chicksList",chicksList);
        return ActionResult.okWithData(map);
    }

}
