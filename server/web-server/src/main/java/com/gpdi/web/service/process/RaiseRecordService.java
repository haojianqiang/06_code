package com.gpdi.web.service.process;

import com.gpdi.web.data.Dict;
import com.gpdi.web.data.process.feeding.FeedingData;
import com.gpdi.web.data.process.prevention.PreventionData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.feed.Feed;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.config.medicine.Medicine;
import com.gpdi.web.entity.config.vaccine.Vaccine;
import com.gpdi.web.entity.process.RaiseRecord;
import com.gpdi.web.entity.vo.ChicksDisVo;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.util.List;

public interface RaiseRecordService {

    QueryResult queryFeed(FeedingData queryData, QuerySettings settings);

    QueryResult queryMedicineOrVaccine(PreventionData queryData, QuerySettings settings);

    RaiseRecord getRaiseRecord(Integer id);

    void save(RaiseRecord raiseRecord);

    void delete(Integer id);

    List<Farm> quertAllFarm();

    List<Feed> queryAllFeed();

    List<Medicine> queryAllMedicine();

    List<Vaccine> queryAllVaccine();

    List<House> queryHouseByFarmId(Integer farmId);

    QueryResult queryChicksCcsByHouseId(Integer houseId);

    List<ChicksDisVo> getChicksDisIdWithBatchNum(Integer houseId);

    List<Dict> queryDict(String cat);

    Dict queryDict(Integer id);

    Chicks queryChicksByChicksCcsId(Integer chicksCcsId);

}
