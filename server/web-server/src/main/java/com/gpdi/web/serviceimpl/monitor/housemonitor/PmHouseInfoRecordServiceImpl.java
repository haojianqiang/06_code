package com.gpdi.web.serviceimpl.monitor.housemonitor;

import com.gpdi.web.dao.monitor.housemonitor.PmHouseInfoRecordDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.config.phone.Phone;
import com.gpdi.web.entity.monitor.housemonitor.PmHouseInfoRecord;
import com.gpdi.web.service.monitor.housemonitor.PmHouseInfoRecordService;
import com.gpdi.web.utils.HttpRequest;
import net.sf.json.JSONArray;
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
//        where+="p.house_id=m.houseId and m.deviceId=s.deviceId";
        if (queryData.getHouseId() != null) {
            where += " and m.houseId = :houseId";
            System.out.println(queryData.getHouseId());
            query.put("houseId", queryData.getHouseId());
        }

        query.select("p.id,p.house_id as 'houseId',p.avg_wight as 'avgWight',p.co2,p.nh3,p.temperature," +
                "p.humidity,p.weather,p.create_time as 'createTime',p.modify_time as 'modifyTime',p.modifier,p.modifier_id as 'modifierId'").from("pm_house_info_record p")
                .orderBy("p.create_time DESC LIMIT 1").where(where);
//        query.select("p.id,m.houseId,p.avg_wight as 'avgWight',s.co2,s.nh3,s.temperature," +
//                "s.humidity,p.weather,s.date as 'createTime',p.modify_time as 'modifyTime',p.modifier,p.modifier_id as 'modifierId'").from("pm_house_info_record p,sys_sensor s,sys_sensor_house_mapping m")
//                .orderBy("s.date DESC LIMIT 1").where(where);
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
    public List<Phone> getPhoneAndId(String farmId) {
        //1、短信通知人下拉框查询;
        //2、根据farmId判断是否已有保存过的号码，没有则提供所有的号码
        //有则根据farmId查询号码和短信内容，同时查询除此之外的其他号码
        //3、相同farmId短信内容相同
        String sql="select distinct farm_id from pm_warning_phone where farm_id=?";
        List count = generalDao.queryList(Phone.class,sql,farmId);
        if (count.size()==0){
            return generalDao.queryList(Phone.class,"SELECT distinct f.phone,'' farm_id,'' introduce FROM `pm_warning_phone` f");
        }else{
            return generalDao.queryList(Phone.class, "SELECT f.phone, f.farm_id, f.introduce FROM `pm_warning_phone` f WHERE farm_id=?\n" +
                    "union all\n" +
                    "SELECT distinct f.phone, '', '' FROM `pm_warning_phone` f\n" +
                    "where phone not in(\n" +
                    "SELECT phone FROM `pm_warning_phone` t WHERE t.farm_id=? \n" +
                    ")",farmId,farmId);
        }
    }

    @Override
    public void savePhone(Phone phone) {
        generalDao.execute("insert into pm_warning_phone(phone,name) values(?,?)",phone.getPhone(),phone.getName());
    }

    /**
     *  已迁移到schedule服务
     */
    //每7分钟时向数据库插入一条实时的传感器数据
    @Scheduled(cron = "0 0/7 * * * ? ")
    public void timingSave(){
        String SensorData= HttpRequest.interfaceUtil("http://www.nolan.net.cn:10023/agr/post/", "[{\"USR\":\"LHD\",\"PW\":\"123456\",\"ID\":\"F528009C0936D7\"}]");

        JSONArray sensor= JSONArray.fromObject(SensorData);
        Object date=sensor.getString(0);
        JSONObject BigDate = JSONObject.fromObject(date);

        String ETMP=BigDate.getString("ETMP");
        String EHUM=BigDate.getString("EHUM");
        String CO2=BigDate.getString("CO2");
        String NH3=BigDate.getString("NH3");
        //强转为float
        Float ETMP1= Float.valueOf(ETMP);
        Float EHUM1= Float.valueOf(EHUM);
        Float CO21= Float.valueOf(CO2);
        Float NH31= Float.valueOf(NH3);
        PmHouseInfoRecord newRecord = null;
        //获取全部鸡舍
        List<Integer> houseIds = generalDao.queryList(Integer.class, "select id from cm_house");
        for(Integer id :houseIds){
            newRecord = new PmHouseInfoRecord();
            newRecord.setHouseId(id);
            newRecord.setCreateTime(new Date());//创建时间
            newRecord.setAvgWight(Float.valueOf("0.2"));//均重
            newRecord.setCo2(CO21);//二氧化碳浓度
            newRecord.setNh3(NH31);//氨气浓度
            newRecord.setTemperature(ETMP1);//温度
            newRecord.setHumidity(EHUM1);//湿度
            save(newRecord);
        }

    }

}
