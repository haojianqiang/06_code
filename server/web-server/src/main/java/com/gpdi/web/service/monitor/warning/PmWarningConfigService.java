package com.gpdi.web.service.monitor.warning;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.monitor.warning.PmWarningConfig;

import java.util.List;

public interface PmWarningConfigService {

    List<PmWarningConfig> query(QueryData queryData);

    void save(PmWarningConfig pmWarningConfig);

    void savePhones(String[] callee, String smsCont, Integer farmId);
}
