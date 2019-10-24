package com.gpdi.web.service.home;

import java.util.Map;

public interface QueryService {
    Map<String, Object> getEarlyWarningQuery(String regionName,Integer farmId,Integer houseId);

    Map<String, Object> getDropdownBoxFarmList(String regionName);

    Map<String, Object> getDropdownHouseBoxList(String farmId);
}
