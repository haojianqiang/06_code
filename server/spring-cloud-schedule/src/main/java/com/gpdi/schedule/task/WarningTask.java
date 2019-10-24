package com.gpdi.schedule.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.gpdi.schedule.dao.SMSInfoDao;
import com.gpdi.schedule.dao.WarningRecordDao;
import com.gpdi.schedule.entity.sms.SMSInfo;
import com.gpdi.schedule.entity.warning.WarningRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.FastHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import pub.constant.CommonRedisKeyConstants;
import pub.dao.GeneralDao;
import pub.dao.jdbc.ListQuery;
import pub.dao.query.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 鸡场预警
 *
 * @author zzj
 * @date 2019/8/14 0014
 */
@Component
@Slf4j
public class WarningTask extends Task {
    public final static String SMS_FLAG_CONFIG_CODE = "sms";
    public final static int SMS_SEND_HOUR_INTERVAL = 1;
    public final static String CONFIG_CODE_LOWER_LIMIT = "_lowerLimit";
    public final static String CONFIG_CODE_UPPER_LIMIT = "_upperLimit";

    @Value("${wx.mini.program.secret}")
    private String secret;


    /**
     * 监控鸡场环境信息
     *
     * @return
     */
    public void execute() {
        List<Map<String, Object>> callList = new ArrayList<>();
        List<WarningRecord> warningRecordList = new ArrayList<>();

        // 鸡棚预警信息
        List<Map<String, Object>> houseList = getHouseInfo();
        // 鸡棚日常记录
        List<Map<String, Object>> houseRecordMapList = getHouseRecord();
        // 禁用预警鸡场
        List<Integer> notWarningFarmIdList = generalDao.queryList(Integer.class,
                "select farm_id from pm_warning_config where config_code = ? and config_value = false", SMS_FLAG_CONFIG_CODE);

        // 遍历鸡场判断是否需要预警
        if (houseList.size() > 0) {
            // 短信内容
            StringBuffer smsSb = new StringBuffer();
            // 将短信内容以鸡棚、鸡场进行归类
            Map<String, Object> preHouseMap = null;
            for (Map<String, Object> houseMap : houseList) {
                // 配置项编码
                Object configCode = houseMap.get("config_code");
                if (configCode == null) {
                    continue;
                }
                // 鸡场id
                Object farmId = houseMap.get("farm_id");
                if (notWarningFarmIdList.contains(farmId)) {
                    continue;
                }
                if (preHouseMap == null) {
                    preHouseMap = houseMap;
                }
                // 按鸡场归类发送预警短信
                if (farmId != preHouseMap.get("farm_id")) {
                    if (smsSb.length() > 0) {
                        if (smsSb.charAt(0) != '鸡') {
                            smsSb.insert(0, "【" + preHouseMap.get("house_name") + "】：\n");
                        }
                        smsSb.insert(0, "【" + preHouseMap.get("farm_name") + "】出现预警项：\n");

                        Map<String, Object> callMap = new FastHashMap();
                        callMap.put("phone_number", preHouseMap.get("phone_number"));
                        callMap.put("sms", smsSb);
                        callMap.put("farm_id", preHouseMap.get("farm_id"));
                        callList.add(callMap);

                        smsSb = new StringBuffer();
                    }
                    preHouseMap = houseMap;
                }

                // 获取鸡棚id
                Object houseId = houseMap.get("house_id");
                // 按鸡棚归类发送预警短信
                if (houseId != houseMap.get("house_id")) {
                    if (smsSb.length() > 0 && smsSb.charAt(0) != '鸡') {
                        smsSb.insert(0, "【" + preHouseMap.get("house_name") + "】：\n");
                    }
                    preHouseMap = houseMap;
                }
                Object configValue = houseMap.get("config_value");
                if (houseId != null && NumberUtil.isNumber(String.valueOf(configValue))) {
                    // 遍历日常记录
                    for (Map<String, Object> houseRecordMap : houseRecordMapList) {
                        // 判断鸡棚日常记录是否超阈值
                        if (houseId.toString().equals(String.valueOf(houseRecordMap.get("houseId")))) {
                            // 下限阈值
                            if (configCode.toString().contains(CONFIG_CODE_LOWER_LIMIT)) {
                                String houseRecordCode = configCode.toString().replace(CONFIG_CODE_LOWER_LIMIT, "");
                                String houseRecordValue = String.valueOf(houseRecordMap.get(houseRecordCode));
                                if (NumberUtil.isNumber(houseRecordValue)) {
                                    if (new BigDecimal(houseRecordValue).compareTo(new BigDecimal(configValue.toString())) == -1) {
                                        String configName = houseMap.get("config_name").toString().replace("下限值", "");
                                        // 预警项信息
                                        WarningRecord warningRecord = new WarningRecord();
                                        warningRecord.setFarmId(((Number) (farmId)).intValue());
                                        warningRecord.setHouseId(((Number) (houseId)).intValue());
                                        warningRecord.setConfigCode(configCode.toString());
                                        warningRecord.setConfigName(configName);
                                        warningRecord.setConfigValue((String) configValue);
                                        warningRecord.setValue(houseRecordValue);
                                        warningRecord.setCreateTime(DateUtil.date());
                                        warningRecordList.add(warningRecord);
                                        // 短信内容
                                        StringBuffer sb = new StringBuffer();
                                        sb.append(configName).append("低于下限阈值：")
                                                .append("预警值为：").append(configValue).append(", ")
                                                .append("当前值为：").append(houseRecordMap.get(houseRecordCode)).append("；\n");
                                        smsSb.insert(0, sb);
                                    }
                                }
                            }
                            // 上限阈值
                            else if (configCode.toString().contains(CONFIG_CODE_UPPER_LIMIT)) {
                                String houseRecordCode = configCode.toString().replace(CONFIG_CODE_UPPER_LIMIT, "");
                                String houseRecordValue = String.valueOf(houseRecordMap.get(houseRecordCode));
                                if (NumberUtil.isNumber(houseRecordValue)) {
                                    if (new BigDecimal(houseRecordValue).compareTo(new BigDecimal(configValue.toString())) == 1) {
                                        String configName = houseMap.get("config_name").toString().replace("上限值", "");
                                        // 预警项信息
                                        WarningRecord warningRecord = new WarningRecord();
                                        warningRecord.setFarmId(((Number) (farmId)).intValue());
                                        warningRecord.setHouseId(((Number) (houseId)).intValue());
                                        warningRecord.setConfigCode(configCode.toString());
                                        warningRecord.setConfigName(configName);
                                        warningRecord.setConfigValue((String) configValue);
                                        warningRecord.setValue(houseRecordValue);
                                        warningRecord.setCreateTime(DateUtil.date());
                                        warningRecordList.add(warningRecord);
                                        // 短信内容
                                        StringBuffer sb = new StringBuffer();
                                        sb.append(configName).append("高于上限阈值：")
                                                .append("预警值为：").append(configValue).append(", ")
                                                .append("当前值为：").append(houseRecordMap.get(houseRecordCode)).append("；\n");
                                        smsSb.insert(0, sb);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (smsSb.length() > 0) {
                if (smsSb.charAt(0) != '鸡') {
                    smsSb.insert(0, "【" + preHouseMap.get("house_name") + "】：\n");
                }
                smsSb.insert(0, "【" + preHouseMap.get("farm_name") + "】出现预警项：\n");

                Map<String, Object> callMap = new FastHashMap();
                callMap.put("phone_number", preHouseMap.get("phone_number"));
                callMap.put("sms", smsSb);
                callMap.put("farm_id", preHouseMap.get("farm_id"));
                callList.add(callMap);
            }
        }

        // 结果
        for (Map<String, Object> callMap : callList) {
            SMSInfo smsInfo = new SMSInfo();
            smsInfo.setSmsCont(callMap.get("sms").toString());
            smsInfo.setCaller("15626042448");
//          smsInfo.setCaller(callMap.get("phone_number").toString());
            smsInfo.setCreateTime(new Date());

            for (WarningRecord warningRecord : warningRecordList) {
                // 判断是否需要保存当前短信，条件：
                // 1.短信未保存
                // 2.预警记录未保存
                // 3.鸡场id相匹配
                boolean sendSmsFlag = smsInfo.getId() == null && warningRecord.getId() == null
                        && warningRecord.getFarmId().equals(callMap.get("farm_id"));
                // 做短信频率处理，如果一个小时之内没有预警短信，才保存当前短信
                if (sendSmsFlag) {
                    Integer preSmsHourDiff = generalDao.queryValue(Integer.class,
                            "select timestampdiff(HOUR, max(create_time), now()) from pm_warning_record " +
                                    "where config_code = ? and house_id = ?", warningRecord.getConfigCode(), warningRecord.getHouseId());
                    sendSmsFlag = preSmsHourDiff == null || preSmsHourDiff >= SMS_SEND_HOUR_INTERVAL;
                }
                // 保存
                if (sendSmsFlag) {
                    smsInfoDao.save(smsInfo);
                }
                boolean saveWarningRecordFlag = smsInfo.getId() != null && warningRecord.getId() == null
                        && warningRecord.getFarmId().equals(callMap.get("farm_id"));
                if (saveWarningRecordFlag) {
                    warningRecord.setSmsId(smsInfo.getId());
                    warningRecordDao.save(warningRecord);
                }
            }
        }
    }


    /**
     * 鸡棚预警信息
     *
     * @return
     */
    private List<Map<String, Object>> getHouseInfo() {
        Query query = new ListQuery();
        query.select("f.id farm_id,f.name farm_name,f.phone_number,h.id house_id,h.name house_name,wc.config_code,wc.config_name,wc.config_value")
                .from("cm_farm f,cm_house h left join pm_warning_config wc on  h.id = wc.house_id")
                .where("f.id = h.farm_id")
                .orderBy("f.id,h.id");
        generalDao.execute(query);
        return (List<Map<String, Object>>) query.getResult().getRows();
    }

    /**
     * 鸡棚日常记录
     *
     * @return
     */
    private List<Map<String, Object>> getHouseRecord() {
        List<Map<String, Object>> resultList = new ArrayList();

        Map<Object, Object> newestHouseInfoMap = redisTemplate.opsForHash().entries(CommonRedisKeyConstants.PM_HOUSE_INFO_RECORD_NEWEST_REDIS_KEY);
        // 先从redis查询
        if (newestHouseInfoMap != null && newestHouseInfoMap.size() > 0) {
            for (Map.Entry<Object, Object> entry : newestHouseInfoMap.entrySet()) {
                resultList.add((Map<String, Object>) JSONUtil.parse(entry.getValue()));
            }
        } else {  // 其次再查库
            Query query = new ListQuery();
            query.select("r.house_id,r.co2,r.nh3,r.temperature,r.humidity")
                    .from("from pm_house_info_record r, " +
                            "(select house_id,max(create_time) create_time from pm_house_info_record group by house_id ) t ")
                    .where("r.house_id = t.house_id and r.create_time = t.create_time")
                    .orderBy("h.id");
            generalDao.execute(query);
            resultList = query.getResult().getRows();
        }

        return resultList;
    }

    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private SMSInfoDao smsInfoDao;
    @Autowired
    private WarningRecordDao warningRecordDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
}
