package com.gpdi.web.service.monitor.warning;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.monitor.warning.PmWarningConfig;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.util.List;

public interface PmWarningConfigService {

    List<PmWarningConfig> query(QueryData queryData);

    void save(PmWarningConfig pmWarningConfig);

}
