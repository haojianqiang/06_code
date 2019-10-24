package com.gpdi.web.serviceimpl.monitor.housemonitor;

import com.gpdi.web.dao.monitor.housemonitor.PmHouseInfoRecordDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.monitor.housemonitor.PmHouseInfoRecord;
import com.gpdi.web.entity.vo.ChicksDisVo;
import com.gpdi.web.service.monitor.housemonitor.PmHouseInfoRecordService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class PmHouseInfoRecordServiceImpl implements PmHouseInfoRecordService {
    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private PmHouseInfoRecordDao pmHouseInfoRecordDao;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public QueryResult query(QueryData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";

        if (queryData.getHouseId() != null) {
            where += " and p.house_id = :houseId";
            System.out.println(queryData.getHouseId());
            query.put("houseId", queryData.getHouseId());
        }

        query.select("p.id,p.house_id as 'houseId',p.avg_wight as 'avgWight',p.co2,p.nh3,p.temperature," +
                "p.humidity,p.weather,p.create_time as 'createTime',p.modify_time as 'modifyTime',p.modifier,p.modifier_id as 'modifierId'").from("pm_house_info_record p")
                .orderBy("p.create_time DESC LIMIT 1").where(where);
        generalDao.execute(query);
        return query.getResult();
    }

    @Override
    public PmHouseInfoRecord getPmHouseInfoRecord(Integer id) {
        return pmHouseInfoRecordDao.get(id);
    }

    @Override
    public void save(PmHouseInfoRecord pmHouseInfoRecord) {
        pmHouseInfoRecordDao.save(pmHouseInfoRecord);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        pmHouseInfoRecordDao.delete(id);
    }

    @Override
    public List<Farm> getFarmNameAndId() {
        return generalDao.queryList(Farm.class, "SELECT f.id, f.name FROM `cm_farm` f");
    }

    @Override
    public List<House> getHouseNameAndId(Integer farmId) {
        return generalDao.queryList(House.class, "select h.id, h.name from cm_house h where h.farm_id = " + farmId);
    }

    @Override
    public List<ChicksDisVo> getBatchNumberAndId(Integer houseId) {
        return generalDao.queryList(ChicksDisVo.class, "select c.id, ck.batch_number batchNumber from ce_chicks_ccs c JOIN cm_chicks ck ON c.chicks_id = ck.id where c.status =  1 AND c.house_id = " + houseId);
    }

    /**
     *  已迁移到schedule服务
     */
    //模拟天气数据 定时向数据库插入 每小时插入一条
//    @Scheduled(cron = "0 0 * * * ?")
    public void timingSave(){
        PmHouseInfoRecord newRecord = null;
        //获取全部鸡舍
        List<Integer> houseIds = generalDao.queryList(Integer.class, "select id from cm_house");
        //存到redis中
        Map<String,String> recordMap= new HashMap<String,String>();
        for(Integer id :houseIds){
            //获取上一条数据
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("select id,house_id as 'houseId',avg_wight as 'avgWight',co2,nh3,temperature,");
            sqlBuilder.append("humidity,weather,create_time as 'createTime',modify_time as 'modifyTime',modifier,modifier_id as 'modifierId'");
            sqlBuilder.append("from pm_house_info_record where house_id = "+id);
            sqlBuilder.append("  ORDER BY create_time DESC LIMIT 1");
            PmHouseInfoRecord last =  generalDao.queryValue(PmHouseInfoRecord.class, sqlBuilder.toString());
            newRecord = new PmHouseInfoRecord();
            newRecord.setHouseId(id);
            newRecord.setCreateTime(new Date());//创建时间
            if(last == null){
                newRecord.setAvgWight(Float.valueOf("0.2"));//均重
                newRecord.setCo2(Float.valueOf("400"));//二氧化碳浓度
                newRecord.setNh3(Float.valueOf("1"));//氨气浓度
                newRecord.setTemperature(Float.valueOf("19"));//温度
                newRecord.setHumidity(Float.valueOf("50"));//湿度
            }else{
                newRecord.setAvgWight(last.getAvgWight() <=5 ? last.getAvgWight()+Float.valueOf("0.1"):last.getAvgWight());//均重
                newRecord.setCo2(last.getCo2() >=900 ? Float.valueOf("400"):last.getCo2()+ Float.valueOf("20"));//二氧化碳浓度 400-900
                newRecord.setNh3( last.getNh3() >= 10 ? Float.valueOf("1"):last.getCo2()+ Float.valueOf("0.1"));//氨气浓度
                newRecord.setTemperature(last.getTemperature()>=30 ? Float.valueOf("19"):last.getTemperature()+Float.valueOf("0.1"));//温度
                newRecord.setHumidity(last.getHumidity() >= 70 ? Float.valueOf("64.0"):last.getHumidity()+Float.valueOf("0.5"));//湿度
            }
            save(newRecord);
            //转换成json
            JSONObject jsonObject = JSONObject.fromObject(newRecord);
            // "鸡舍id" ："{鸡舍环境信息json}"
            recordMap.put(id.toString(),jsonObject.toString());
        }
        //将Map存到redis中
        redisTemplate.opsForHash().putAll("pm_house_info_record:newest",recordMap);
    }

}
