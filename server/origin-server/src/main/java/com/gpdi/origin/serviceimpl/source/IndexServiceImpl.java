package com.gpdi.origin.serviceimpl.source;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.gpdi.origin.service.source.IndexService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.dao.GeneralDao;
import pub.dao.jdbc.ListQuery;
import pub.dao.query.Query;

import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    @Override
    public Map<String, Object> traceOrigin(String originCode) {
        Map<String, Object> resultMap = new HashedMap();

        // 查询鸡苗信息
        Query query = new ListQuery();
        query.select("c.batch_number, c.chick_type, c.producer, ccs.id, ccs.house_id, ccs.create_time, ccs.chick_count, " +
                "ccs.produce_cycle, ccs.modifier")
                .from("cm_chicks c, ce_chicks_ccs ccs")
                .where(StrUtil.format("ccs.chicks_id = c.id and ccs.origin_code = {}", originCode));
        generalDao.execute(query);
        List chicksList = query.getResult().getRows();
        if (chicksList.size() == 0) {
            return resultMap;
        }
        resultMap.put("chicks", chicksList);
        Map<String, Object> chicks = (Map<String, Object>) chicksList.get(0);
        // 鸡苗id
        Object chicksCcsId = chicks.get("id");
        // 鸡苗所在鸡棚id
        Object houseId = chicks.get("house_id");

        // 查询农场主信息
        if (ObjectUtil.isNotNull(houseId)) {
            query.select("f.*").from("cm_farm f, cm_house h")
                    .where(StrUtil.format("h.farm_id = f.id and h.id =  {}", houseId));
            generalDao.execute(query);
            resultMap.put("farm", query.getResult().getRows());
        }


        // 查询用料信息
        query.select("f.name,f.producer,rr.raise_value,rr.raise_time,rr.raise_reason,rr.modifier,rr.chick_day_age")
                .from("cm_feed f, pm_raise_record rr")
                .where(StrUtil.format("f.name = rr.raise_name and rr.chicks_ccs_id =  {}", chicksCcsId))
                .orderBy("rr.raise_name");
        generalDao.execute(query);
        resultMap.put("feed", query.getResult().getRows());

        // 查询用药信息
        query.select("m.name,m.producer,rr.raise_value,rr.raise_time,rr.raise_reason,rr.modifier,rr.chick_day_age")
                .from("cm_medicine m, pm_raise_record rr")
                .where(StrUtil.format("m.name = rr.raise_name and rr.chicks_ccs_id = {}", chicksCcsId))
                .orderBy("rr.raise_name");
        generalDao.execute(query);
        resultMap.put("medicine", query.getResult().getRows());

        // 查询疫苗信息
        query.select("v.name,v.producer,rr.raise_value,rr.raise_time,rr.raise_reason,rr.modifier,rr.chick_day_age")
                .from("cm_vaccine v, pm_raise_record rr")
                .where(StrUtil.format("v.name = rr.raise_name and rr.chicks_ccs_id =  {}", chicksCcsId))
                .orderBy("rr.raise_name");
        generalDao.execute(query);
        resultMap.put("vaccine", query.getResult().getRows());

        return resultMap;
    }


    @Autowired
    private GeneralDao generalDao;

}
