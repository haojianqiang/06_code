package com.gpdi.web.service.process;

import com.gpdi.web.data.Dict;
import com.gpdi.web.data.process.deathElimination.EliminationData;
import com.gpdi.web.data.process.feeding.FeedingData;
import com.gpdi.web.data.process.prevention.PreventionData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.feed.Feed;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.config.medicine.Medicine;
import com.gpdi.web.entity.config.vaccine.Vaccine;
import com.gpdi.web.entity.process.RaiseRecord;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.util.List;

public interface DeathEliminationService {

    QueryResult query(EliminationData queryData, QuerySettings settings);

}
