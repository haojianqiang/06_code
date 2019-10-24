package com.gpdi.web.web.process.prevention.action;

import com.gpdi.web.data.Dict;
import com.gpdi.web.data.process.prevention.PreventionData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.config.medicine.Medicine;
import com.gpdi.web.entity.config.vaccine.Vaccine;
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
    public QueryResult query(PreventionData queryData, PageSettings settings){
        return raiseRecordService.queryMedicineOrVaccine(queryData,settings);
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
        if(raiseRecord.getRaiseType() == 1){
            List<Medicine> listMedicine = raiseRecordService.queryAllMedicine();
            map.put("listRaise",listMedicine);
        }else {
            List<Vaccine> listVaccine = raiseRecordService.queryAllVaccine();
            map.put("listRaise",listVaccine);
        }
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

        List<Medicine> listMedicine = raiseRecordService.queryAllMedicine();
        List<Vaccine> listVaccine = raiseRecordService.queryAllVaccine();
        List<Dict> listRaiseType = raiseRecordService.queryDict("pm_raise_record.raise_type");
        Iterator typeIterator = listRaiseType.iterator();
        while(typeIterator.hasNext()){
            Dict dict = (Dict)typeIterator.next();
            if (dict.getCode() == 0) {
                typeIterator.remove();
            }
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("listFarm",listFarm);
        map.put("listMedicine",listMedicine);
        map.put("listVaccine",listVaccine);
        map.put("listRaiseType",listRaiseType);
        map.put("listFarmTree",farmTreeListFarm);
        return ActionResult.okWithData(map);
    }

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
    public ActionResult queryAllMedicine() {
        List<Medicine> listMedicine = raiseRecordService.queryAllMedicine();
        return ActionResult.okWithData(listMedicine);
    }

    @RequestMapping
    public ActionResult queryAllVaccine() {
        List<Vaccine> listVaccine = raiseRecordService.queryAllVaccine();
        return ActionResult.okWithData(listVaccine);
    }

    @RequestMapping
    public QueryResult queryChicksCcs(Integer houseId){
        return raiseRecordService.queryChicksCcsByHouseId(houseId);
    }

    @RequestMapping
    public ActionResult queryRaiseType(Integer raiseType) {
        Dict dict = raiseRecordService.queryDict(raiseType);
        ActionResult result = new ActionResult();
        if(dict.getCode() == 1){
            List<Medicine> listMedicine = raiseRecordService.queryAllMedicine();
            result.setData(listMedicine);
        }else if(dict.getCode() == 2){
            List<Vaccine> listVaccine = raiseRecordService.queryAllVaccine();
            result.setData(listVaccine);
        }
        return result;
    }

}
