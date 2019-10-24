package com.gpdi.web.service.config.farm;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.user.SysUser;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.text.ParseException;
import java.util.List;

public interface FarmService {

    QueryResult query(QueryData queryData, QuerySettings settings);

    Farm getFarm(Integer id);

    void save(Farm farm);

    void delete(Integer id);

}
