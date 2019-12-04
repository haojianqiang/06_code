package com.gpdi.web.serviceimpl.home;


import com.gpdi.web.service.home.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.ListQuery;
import pub.dao.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = false)
public class QueryServiceImpl implements QueryService {
    @Override
    public Map<String, Object> getEarlyWarningQuery(String regionName,Integer farmId,Integer houseId) {
        Query query = new ListQuery();
        Map<String, Object> map = new HashMap<>();
        //气象数据
        String whereMeteorological = " where 1=1 ";
        if(regionName != null){
            whereMeteorological += " and town = '"+regionName +"'";
        }
        String sql = "SELECT town 区域,temperature 温度,concat(ceil(humidity*100),'%') 湿度,concat(wind_direction,wind_level,'级') 风力风向,\n" +
                " meteorology 气象情况, (case when ifnull(aqi,0)<=50  then '优'\n" +
                " when ifnull(aqi,0)>51 and ifnull(aqi,0)<=100 then '良'\n" +
                " when ifnull(aqi,0)>100 and ifnull(aqi,0)<=150  then '轻度污染'\n" +
                " when ifnull(aqi,0)>150 and ifnull(aqi,0)<=200 then '中度污染'\n" +
                " when ifnull(aqi,0)>200 and ifnull(aqi,0)<=300 then '重度污染'\n" +
                " when ifnull(aqi,0)>300 then '严重污染' end) 空气质量比,ifnull(precipitation,0) 降水量\n" +
                " FROM cm_weather " +whereMeteorological+ " ORDER BY create_time desc LIMIT 1";
        query.setSql(sql);
        generalDao.execute(query);
        map.put("meteorological", query.getResult().getRows());
        //存栏量信息
        String whereStock = " where 1=1 ";
        if(regionName != null){
            whereStock += " and cf.town = '"+regionName +"'";
        }
        if (farmId != null){
            whereStock += " and ch.farm_id = "+farmId;
        }
        if (houseId != null){
            whereStock += " and ch.id = "+houseId;
        }
        sql = "SELECT ch.NAME 鸡棚,sum( a.chick_count - ifnull( b.count, 0 ) ) 存栏量 " +
                " FROM ce_chicks_ccs a LEFT JOIN pm_death_elimination_record b ON a.id = b.chicks_ccs_id," +
                " (SELECT ch.id,ch.NAME,ch.farm_id FROM cm_house ch JOIN cm_farm cf ON cf.id = ch.farm_id " +whereStock+" ) ch " +
                " WHERE ch.id = a.house_id AND a.STATUS = 1 " +
                " GROUP BY ch.NAME";
        query.setSql(sql);
        generalDao.execute(query);
        query.getResult().getRows();
        map.put("stock", query.getResult().getRows());
        //预警信息
        String whereWarning = " where 1=1";
        if(regionName != null){
            whereWarning += " and cf.town = '"+regionName +"'";
        }
        if(farmId != null){
            whereWarning += " and pwr.farm_id ="+farmId;
        }
        if(houseId != null){
            whereWarning += " and pwr.house_id ="+houseId;
        }
        sql = "SELECT cf.NAME farmName, ch.NAME houseName,pwr.config_value configValue,pwr.value" +
                " FROM pm_warning_record pwr JOIN cm_farm cf ON pwr.farm_id = cf.id JOIN cm_house ch ON pwr.house_id = ch.id " +
                 whereWarning +
                " ORDER BY pwr.create_time DESC " +
                " LIMIT 20";
        query.setSql(sql);
        generalDao.execute(query);
        List list = query.getResult().getRows();
        map.put("warning", query.getResult().getRows());
        //湿度/二氧化碳/温度/氨气
        String whereHumidityOnCo2 = " ";
        if(regionName != null){
            whereHumidityOnCo2 += " and cf.town = '"+regionName+"'";
        }
        if(farmId != null){
            whereHumidityOnCo2 += " and cf.id = "+farmId;
        }
        if(houseId != null){
            whereHumidityOnCo2 += " and ch.id = "+houseId;
        }
//        sql = " SELECT b.name 鸡棚,date_format(a.create_time,'%H') 时间,humidity 湿度,co2 二氧化碳" +
//              " ,temperature 温度,nh3 氨气 from pm_house_info_record a,(SELECT ch.id,ch.name,ch.farm_id from cm_house ch," +
//              " cm_farm cf where cf.id =ch.farm_id "+whereHumidityOnCo2+") b " +
//              " where a.house_id =b.id " +
//              " and date_format(a.create_time, '%Y%m%d') =date_format(DATE_SUB(CURDATE(),INTERVAL 1 DAY), '%Y%m%d')" +
//              " and date_format(a.create_time,'%H') in ('10','12','14','16','18')";
        sql = "SELECT b.name 鸡棚,date_format(a.date,'%H') 时间,humidity 湿度,co2 二氧化碳\n" +
                ",temperature 温度,nh3 氨气 from (select * from (select * from sys_sensor order by date DESC) a GROUP BY a.deviceId,date_format(a.date,'%H') ORDER BY deviceId desc) a," +
                "(SELECT ch.id,ch.name,ch.farm_id from cm_house ch,\n" +
                "cm_farm cf where cf.id =ch.farm_id "+whereHumidityOnCo2+") b ,sys_sensor_house_mapping c\n" +
                "where a.deviceId=c.deviceId and c.houseId=b.id\n" +
                "and date_format(a.date, '%Y%m%d') =date_format(DATE_SUB(CURDATE(),INTERVAL 3 DAY), '%Y%m%d')" +
                " and date_format(a.date,'%H') in ('10','12','14','16','18')";
        query.setSql(sql);
        generalDao.execute(query);
        query.getResult().getRows();
        map.put("HumidityOnCo2", query.getResult().getRows());
        //饲料名称,饲料用量
        String whereFeed = " ";
        if(regionName != null){
            whereFeed += " and d.town = '"+regionName +"'";
        }
        if(farmId != null){
            whereFeed += " and d.id = "+farmId;
        }
        if(houseId != null){
            whereFeed += " and c.id = "+houseId;
        }
        sql = "SELECT a.raise_name feedName,sum( a.raise_value ) feedDosage " +
                " FROM " +
                " pm_raise_record a " +
                " JOIN ce_chicks_ccs b ON a.chicks_ccs_id = b.id " +
                " JOIN cm_house c ON b.house_id = c.id" +
                " JOIN cm_farm d ON c.farm_id = d.id " +
                " JOIN sys_dict e ON a.raise_type = e.id" +
                " WHERE" +
                " e.code = 0" +
                " AND b.STATUS = 1" +
                whereFeed +
                " GROUP BY" +
                " a.raise_name";
        query.setSql(sql);
        generalDao.execute(query);
        query.getResult().getRows();
        map.put("feed", query.getResult().getRows());
        //药物名称,药物用量
        String whereMedicine = " ";
        if(regionName != null){
            whereMedicine += " and d.town = '"+regionName +"'";
        }
        if(farmId != null){
            whereMedicine += " and d.id = "+farmId;
        }
        if(houseId != null){
            whereMedicine += " and c.id = "+houseId;
        }
        sql = "SELECT a.raise_name medicineName,sum( a.raise_value ) medicineDosage " +
                " FROM " +
                " pm_raise_record a " +
                " JOIN ce_chicks_ccs b ON a.chicks_ccs_id = b.id " +
                " JOIN cm_house c ON b.house_id = c.id" +
                " JOIN cm_farm d ON c.farm_id = d.id " +
                " JOIN sys_dict e ON a.raise_type = e.id" +
                " WHERE" +
                " (e.code = 1 or e.code = 2)" +
                " AND b.STATUS = 1" +
                whereMedicine +
                " GROUP BY" +
                " a.raise_name";
        query.setSql(sql);
        generalDao.execute(query);
        query.getResult().getRows();
        map.put("medicine", query.getResult().getRows());
        //育成率、死亡率百分比
        String whereBreedingDeath = " ";
        if(regionName != null){
            whereBreedingDeath += " and cf.town = '"+regionName +"'";
        }
        if(farmId != null){
            whereBreedingDeath += " and cf.id = "+farmId;
        }
        if(houseId != null){
            whereBreedingDeath += " and ch.id = "+houseId;
        }
        sql = " select sum(a.chick_count) 存栏量,sum(ifnull(b.count,0)) 死亡数, " +
                " case when sum(a.chick_count) !=0  then ROUND((sum(a.chick_count)-sum(ifnull(b.count,0)))/sum(a.chick_count)*100,2) else 100 end 育成率, " +
                " case when sum(a.chick_count) !=0  then ROUND(sum(ifnull(b.count,0))/sum(a.chick_count)*100,2) else 0 end 死亡率 " +
                " from ce_chicks_ccs a LEFT JOIN pm_death_elimination_record b on a.id =b.chicks_ccs_id,(" +
                " SELECT ch.id,ch.name,ch.farm_id from cm_house ch,cm_farm cf where cf.id =ch.farm_id " + whereBreedingDeath + ") ch " +
                " where ch.id =a.house_id and a.status =1";
        query.setSql(sql);
        generalDao.execute(query);
        query.getResult().getRows();
        map.put("breedingDeath", query.getResult().getRows());
        //肉鸡出存栏信息
        String whereColumn = " ";
        if(regionName != null){
            whereColumn += " and c.town = '"+regionName +"'";
        }
        if(farmId != null){
            whereColumn += " and c.id = "+farmId;
        }
        if(houseId != null){
            whereColumn += " and b.id = "+houseId;
        }
        sql = "SELECT" +
                " b.name 鸡棚,sum( a.chick_count ) 存栏,sum(d.sale_count) 出栏" +
                " FROM" +
                " ce_chicks_ccs a" +
                " JOIN cm_house b ON a.house_id = b.id" +
                " JOIN cm_farm c ON b.farm_id = c.id" +
                " LEFT JOIN pe_sale_record_ccs d ON d.house_id = b.id" +
                " WHERE" +
                " a.status = 1" +
                  whereColumn +
                " GROUP BY" +
                " a.house_id";
        query.setSql(sql);
        generalDao.execute(query);
        query.getResult().getRows();
        map.put("columnInfo", query.getResult().getRows());
        //成交信息
        String whereProduction = " ";
        if(regionName != null){
            whereProduction += " and c.town = '"+regionName +"'";
        }
        if(farmId != null){
            whereProduction += " and c.id = "+farmId;
        }
        if(houseId != null){
            whereProduction += " and b.id = "+houseId;
        }
        sql = "SELECT" +
                " sum( count )/10000 成交量," +
                " sum( total )/10000 成交金额," +
                " DATE_FORMAT( sale_time, '%Y-%m' ) 时间" +
                " FROM" +
                " pm_sale_record " +
                " WHERE 1=1" +
                " AND DATE_FORMAT( sale_time, '%Y') = 2019" +
                " GROUP BY" +
                " ( DATE_FORMAT( sale_time, '%Y-%m' ) )";
        query.setSql(sql);
        generalDao.execute(query);
        query.getResult().getRows();
        map.put("productionInfo", query.getResult().getRows());
        return map;
    }

    @Override
    public Map<String, Object> getDropdownBoxFarmList(String regionName) {
        Query query = new ListQuery();
        Map<String, Object> map = new HashMap<>();
        //鸡场下拉框查询
        String sql = "SELECT id value,name label FROM cm_farm where town ='"+regionName+"' ";
        query.setSql(sql);
        generalDao.execute(query);
        query.getResult().getRows();
        map.put("dropdownBoxFarmList", query.getResult().getRows());
        return map;
    }

    @Override
    public Map<String, Object> getDropdownHouseBoxList(String farmId) {
        Query query = new ListQuery();
        Map<String, Object> map = new HashMap<>();
        //鸡舍下拉框查询
        String  sql = "SELECT id value,name label FROM cm_house where farm_id ='"+farmId+"'";
        query.setSql(sql);
        generalDao.execute(query);
        query.getResult().getRows();
        map.put("dropdownBoxHouseList", query.getResult().getRows());
        return map;
    }

    @Autowired
    private GeneralDao generalDao;
}
