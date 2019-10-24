package com.gpdi.web.service.config.medicine;

import com.gpdi.web.data.QueryData;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;
import com.gpdi.web.entity.config.medicine.Medicine;

public interface MedicineService {
    QueryResult query(QueryData queryData, QuerySettings settings);

    Medicine getMedicine(Integer id);

    void save(Medicine medicine);

    void delete(Integer id);
}
