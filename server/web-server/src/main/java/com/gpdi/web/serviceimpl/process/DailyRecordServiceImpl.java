package com.gpdi.web.serviceimpl.process;

import com.gpdi.web.dao.config.farm.FarmDao;
import com.gpdi.web.dao.config.feed.FeedDao;
import com.gpdi.web.dao.config.medicine.MedicineDao;
import com.gpdi.web.dao.config.vaccine.VaccineDao;
import com.gpdi.web.dao.process.RaiseRecordDao;
import com.gpdi.web.data.process.dailyRecord.DailyData;
import com.gpdi.web.data.process.deathElimination.EliminationData;
import com.gpdi.web.service.process.DailyRecordService;
import com.gpdi.web.service.process.DeathEliminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

@Service
@Transactional(readOnly = false)
public class DailyRecordServiceImpl implements DailyRecordService {
    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private RaiseRecordDao raiseRecordDao;
    @Autowired
    private FarmDao farmDao;
    @Autowired
    private FeedDao feedDao;
    @Autowired
    private MedicineDao medicineDao;
    @Autowired
    private VaccineDao vaccineDao;

    @Override
    public QueryResult query(DailyData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (queryData.getFarmId() != null) {
            where += " and f.id = :farmId";
            query.put("farmId", queryData.getFarmId());
        }
        if (queryData.getHouseId() != null) {
            where += " and h.id = :houseId";
            query.put("houseId", queryData.getHouseId());
        }
        if (queryData.getChicksCcsId() != null) {
            where += " and cc.id = :chicksCcsId";
            query.put("chicksCcsId", queryData.getChicksCcsId());
        }

        query.select("dr.id,dr.avg_weight as avgWeight,dr.water,dr.hygiene,dr.remark,dr.create_time as createTime,f.id as farmId,h.id as houseId,dr.chicks_ccs_id as chicksCcsId,f.name as farmName,h.name as houseName,c.batch_number as batchNumber")
                .from("pm_daily_record dr join ce_chicks_ccs cc on dr.chicks_ccs_id = cc.id join cm_chicks c on cc.chicks_id = c.id join cm_house h on cc.house_id = h.id join cm_farm f on h.farm_id = f.id")
                .where(where);
        generalDao.execute(query);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

}
