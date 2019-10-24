package com.gpdi.web.service.config.chicks;


import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.vo.ChicksTransactionVo;
import com.gpdi.web.entity.vo.ProductionVo;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.util.List;

public interface ChicksService {
    QueryResult query(QueryData queryData, QuerySettings settings);

    Chicks getChicks(Integer id);

    void save(Chicks chicks);

    void delete(Integer id);

    List<Farm> getFarmNameAndId();

    List<ChicksTransactionVo> getTransactionInfoByHouseid(Integer houseid);

    List<ProductionVo> getProductionInfoByDate(String startTime, String endTime);
}
