package com.gpdi.web.service.config.house;


import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.util.List;

public interface HouseService {
    QueryResult query(QueryData queryData, QuerySettings settings);

    House getHouse(Integer id);

    void save(House house);

    void delete(Integer id);

    List<Farm> getFarmNameAndId();

    List<House> getHouseByFarm_id(Integer id);
}
