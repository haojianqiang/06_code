package com.gpdi.web.service.process;

import com.gpdi.web.data.process.dailyRecord.DailyData;
import com.gpdi.web.data.process.deathElimination.EliminationData;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

public interface DailyRecordService {

    QueryResult query(DailyData queryData, QuerySettings settings);

}
