package com.gpdi.web.service.monitor.housemonitor;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.config.phone.Phone;
import com.gpdi.web.entity.monitor.housemonitor.PmHouseInfoRecord;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.util.List;

public interface PmHouseInfoRecordService {
    QueryResult query(QueryData queryData, QuerySettings settings);

    PmHouseInfoRecord getPmHouseInfoRecord(Integer id);

    void save(PmHouseInfoRecord pmHouseInfoRecord);

    void delete(Integer id);

    List<Farm> getFarmNameAndId();

    List<House> getHouseNameAndId(Integer farmId);

    List<Phone> getPhoneAndId(String farmId);

    void savePhone(Phone phone);
}
