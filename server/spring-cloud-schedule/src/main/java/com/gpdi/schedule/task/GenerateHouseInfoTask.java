package com.gpdi.schedule.task;

import com.gpdi.schedule.dao.HouseInfoRecordDao;
import com.gpdi.schedule.entity.house.HouseInfoRecord;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import pub.constant.CommonRedisKeyConstants;
import pub.dao.GeneralDao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zzj
 * @date 2019/8/27 0027
 */
@Slf4j
@Component
public class GenerateHouseInfoTask extends Task {

    /**
     * 模拟天气数据 定时向数据库插入 每小时插入一条
     *
     * @return
     */
    public void execute() {
        HouseInfoRecord newRecord = null;
        //获取全部鸡舍
        List<Integer> houseIds = generalDao.queryList(Integer.class, "select id from cm_house");
        //存到redis中
        Map<String, String> recordMap = new HashMap<String, String>();
        for (Integer id : houseIds) {
            //获取上一条数据
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("select id,house_id as 'houseId',avg_wight as 'avgWight',co2,nh3,temperature,");
            sqlBuilder.append(" humidity,weather,create_time as 'createTime',modify_time as 'modifyTime',modifier,modifier_id as 'modifierId'");
            sqlBuilder.append(" from pm_house_info_record where house_id = " + id);
            sqlBuilder.append(" order by create_time desc limit 1");
            HouseInfoRecord last = generalDao.queryValue(HouseInfoRecord.class, sqlBuilder.toString());
            newRecord = new HouseInfoRecord();
            newRecord.setHouseId(id);
            newRecord.setCreateTime(new Date());//创建时间
            if (last == null) {
                newRecord.setAvgWight(Float.valueOf("0.2"));//均重
                newRecord.setCo2(Float.valueOf("400"));//二氧化碳浓度
                newRecord.setNh3(Float.valueOf("1"));//氨气浓度
                newRecord.setTemperature(Float.valueOf("19"));//温度
                newRecord.setHumidity(Float.valueOf("50"));//湿度
            } else {
                newRecord.setAvgWight(last.getAvgWight() <= 5 ? last.getAvgWight() + Float.valueOf("0.1") : last.getAvgWight());//均重
                newRecord.setCo2(last.getCo2() >= 900 ? Float.valueOf("400") : last.getCo2() + Float.valueOf("20"));//二氧化碳浓度 400-900
                newRecord.setNh3(last.getNh3() >= 10 ? Float.valueOf("1") : last.getNh3() + Float.valueOf("0.1"));//氨气浓度
                newRecord.setTemperature(last.getTemperature() >= 30 ? Float.valueOf("19") : last.getTemperature() + Float.valueOf("0.1"));//温度
                newRecord.setHumidity(last.getHumidity() >= 70 ? Float.valueOf("64.0") : last.getHumidity() + Float.valueOf("0.5"));//湿度
            }
            houseInfoRecordDao.save(newRecord);
            //转换成json
            JSONObject jsonObject = JSONObject.fromObject(newRecord);
            // "鸡舍id" ："{鸡舍环境信息json}"
            recordMap.put(id.toString(), jsonObject.toString());
        }
        //将Map存到redis中
        redisTemplate.opsForHash().putAll(CommonRedisKeyConstants.PM_HOUSE_INFO_RECORD_NEWEST_REDIS_KEY, recordMap);
    }


    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private HouseInfoRecordDao houseInfoRecordDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
}
