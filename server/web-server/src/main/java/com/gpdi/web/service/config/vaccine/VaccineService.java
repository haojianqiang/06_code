package com.gpdi.web.service.config.vaccine;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.vaccine.Vaccine;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

public interface VaccineService {
    QueryResult query(QueryData queryData, QuerySettings settings);

    Vaccine getVaccine(Integer id);

    void save(Vaccine vaccine);

    void delete(Integer id);
}
