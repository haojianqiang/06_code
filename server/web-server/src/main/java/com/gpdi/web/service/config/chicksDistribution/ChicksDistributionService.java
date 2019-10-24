package com.gpdi.web.service.config.chicksDistribution;

import com.gpdi.web.data.ChicksDistributionData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.chicksDistribution.ChicksDistribution;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.vo.HouseVo;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.util.List;

public interface ChicksDistributionService {
    QueryResult query(ChicksDistributionData queryData, QuerySettings settings);

    ChicksDistribution getChicksDistribution(Integer id);

    void save(ChicksDistribution chicksDistribution);

    void delete(Integer id);

    List<Farm> getFarmNameAndId();

    List<House> getHouseNameAndId(Integer farmId);

    List<Chicks> getChicksBatchNumAndId(Integer farmId);

    List<Chicks> getChicksBatchNumByHouseId(Integer houseId);

    List<House> getHouseNameAndIdByFarmId(Integer farmId);

    List<Chicks> getChicksBatchNumAndTypeAndProducerAndId();

    ChicksDistribution queryChicksDistribution(ChicksDistribution chicksDistribution);

    List<HouseVo> getUnDistributionHouse(Integer farmId);

    HouseVo getDistributionHouse(Integer chicksDistributionId);

    List<ChicksDistribution>   getChick_countByHouseid(Integer Houseid);

}
