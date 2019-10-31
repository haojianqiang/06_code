package com.gpdi.web.web.monitor.warning.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.monitor.warning.PmWarningConfig;
import com.gpdi.web.service.monitor.warning.PmWarningConfigService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pub.spring.web.mvc.ActionResult;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class QueryAction {
    private final static Logger logger = LoggerFactory.getLogger(QueryAction.class);

    @Autowired
    private PmWarningConfigService pmWarningConfigService;

    @RequestMapping
    public List<PmWarningConfig> query(QueryData queryData) {

        return pmWarningConfigService.query(queryData);
    }

    //保存阈值
    @RequestMapping
    public ActionResult saveThresholdValue( String pmWarningConfigs ,Integer farmId, String houseIds) {

        List<PmWarningConfig> configList = (List<PmWarningConfig>)JSONArray.toCollection(JSONArray.fromObject(pmWarningConfigs), PmWarningConfig.class);
        List<Integer> houseIdList = (List<Integer>)JSONArray.toCollection(JSONArray.fromObject(houseIds), Integer.class);
        //八个阈值的键值对
        Map congfigMap = new HashMap<String,Integer>();
        try {
            for (Integer houseId : houseIdList){
                //先查看该鸡棚记录是否已经存在，存在则更新，不存在则新增
                QueryData queryData = new QueryData();
                queryData.setFarmId(farmId);
                queryData.setHouseId(houseId);
                List<PmWarningConfig> oldConfigs = pmWarningConfigService.query(queryData);
                if(oldConfigs.size() >0){
                    for(PmWarningConfig item :oldConfigs){
                        congfigMap.put(item.getConfigCode(),item.getId());
                    }
                }
                for (PmWarningConfig item :configList){
                    if(oldConfigs.size() >0){
                        item.setId((Integer)congfigMap.get(item.getConfigCode()));
                    }
                    if (item.getId() == null) {
                        item.setCreateTime(new Date());
                    } else {
                        item.setModifyTime(new Date());
                    }
                    item.setFarmId(farmId);
                    item.setHouseId(houseId);
                    pmWarningConfigService.save(item);
                    item.setId(null);
                }
            }
            return ActionResult.ok();
        } catch (Exception e) {
            return ActionResult.error(e);
        }
    }
    //保存预警内容或预警方式，以及短信联系人和短信内容
    @RequestMapping
    public ActionResult save (String pmWarningConfigs ,Integer farmId, Integer houseId,String smsCont,String caller) {
        List<PmWarningConfig> configList = (List<PmWarningConfig>)JSONArray.toCollection(JSONArray.fromObject(pmWarningConfigs), PmWarningConfig.class);
        try {
            for (PmWarningConfig item :configList){
                if (item.getId() != null) {
                    item.setModifyTime(new Date());
                } else {
                    item.setCreateTime(new Date());
                }
                item.setFarmId(farmId);
                item.setHouseId(houseId);
                pmWarningConfigService.save(item);

            }
            List<String> callers = (List<String>) JSONArray.toCollection(JSONArray.fromObject(caller), List.class);
            String[] callee = callers.toArray(new String[callers.size()]);
            pmWarningConfigService.savePhones(callee,smsCont,farmId);

            return ActionResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }
}
