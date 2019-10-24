package com.gpdi.web.serviceimpl.config.house;

import com.gpdi.web.dao.config.house.HouseDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.service.config.house.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;
import pub.functions.StrFuncs;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class HouseServiceImpl implements HouseService {
    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private HouseDao houseDao;

    @Override
    public QueryResult query(QueryData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (StrFuncs.notEmpty(queryData.getFarmName())) {
            where += " and f.name like :farmName";
            query.put("farmName", StrFuncs.anyLike(queryData.getFarmName()));
        }
        if (StrFuncs.notEmpty(queryData.getHouseName())) {
            where += " and h.name like :houseName";
            query.put("houseName", StrFuncs.anyLike(queryData.getHouseName()));
        }
        if (queryData.getCapacity() != null) {
            where += " and h.capacity = :capacity";
            query.put("capacity", queryData.getCapacity());
        }
        if (StrFuncs.notEmpty(queryData.getAddress())) {
            where += " and f.address like :address";
            query.put("address", StrFuncs.anyLike(queryData.getAddress()));
        }
        query.select("\th.id,\n" +
                "\tf. NAME as 'farm_name',\n" +
                "\tf.address,\n" +
                "\th. NAME as 'house_name',\n" +
                "\th.length,\n" +
                "\th.width,\n" +
                "\th.height,\n" +
                "\th.capacity,\n" +
                "\th.charger,\n" +
                "\th.create_time,\n" +
                "\th.modify_time,\n" +
                "\th.modifier,\n" +
                "\th.modifier_id").from("cm_house h left join cm_farm f ON h.farm_id = f.id")
                .where(where)
                .orderBy("create_time desc");

        generalDao.execute(query);
        return query.getResult();
    }

    @Override
    public House getHouse(Integer id) {
        return houseDao.get(id);
    }

    @Override
    public void save(House house) {
        houseDao.save(house);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        houseDao.delete(id);
    }

    @Override
    public List<Farm> getFarmNameAndId() {
        return generalDao.queryList(Farm.class, "SELECT f.id, f.name FROM `cm_farm` f");
    }

    @Override
    public List<House> getHouseByFarm_id(Integer id) {
        //通过鸡场id查询鸡棚数
        return generalDao.queryList(House.class,"SELECT name FROM cm_house  where farm_id = ?",id);
    }

}
