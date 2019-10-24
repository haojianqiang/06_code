package com.gpdi.web.serviceimpl.process;

import com.gpdi.web.dao.config.farm.FarmDao;
import com.gpdi.web.dao.config.feed.FeedDao;
import com.gpdi.web.dao.config.medicine.MedicineDao;
import com.gpdi.web.dao.config.vaccine.VaccineDao;
import com.gpdi.web.dao.process.RaiseRecordDao;
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
import com.gpdi.web.service.process.RaiseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.ListQuery;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;
import pub.functions.StrFuncs;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class RaiseRecordServiceImpl implements RaiseRecordService {
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
    public QueryResult queryFeed(FeedingData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (StrFuncs.notEmpty(queryData.getRaiseTimeStart())) {
            where += " and rr.raise_time >= :raiseTimeStart";
            query.put("raiseTimeStart", queryData.getRaiseTimeStart());
        }
        if (StrFuncs.notEmpty(queryData.getRaiseTimeEnd())) {
            where += " and rr.raise_time <= :raiseTimeEnd";
            query.put("raiseTimeEnd", queryData.getRaiseTimeEnd());
        }
        if (queryData.getFarmId() != null) {
            where += " and f.id = :farmId";
            query.put("farmId", queryData.getFarmId());
        }
        if (queryData.getHouseId() != null) {
            where += " and h.id = :houseId";
            query.put("houseId", queryData.getHouseId());
        }
        if(queryData.getRaiseId() == null){
            where += " and rr.raise_type = 1";
        }else {
            where += " and rr.raise_id = :raiseId and rr.raise_type = 1";
            query.put("raiseId", queryData.getRaiseId());
        }
        if (queryData.getRaiseValueStart() != null) {
            where += " and rr.raise_value >= :raiseValueStart";
            query.put("raiseValueStart", queryData.getRaiseValueStart());
        }
        if (queryData.getRaiseValueEnd() != null) {
            where += " and rr.raise_value <= :raiseValueEnd";
            query.put("raiseValueEnd", queryData.getRaiseValueEnd());
        }
        if (queryData.getChicksCcsId() != null) {
            where += " and cc.id = :chicksCcsId";
            query.put("chicksCcsId", queryData.getChicksCcsId());
        }

        query.select("rr.id,f.id as farmId,h.id as houseId,rr.chicks_ccs_id as chicksCcsId,f.name as farmName,h.name as houseName,rr.raise_name as raiseName,rr.raise_value as raiseValue,rr.raise_time as raiseTime")
                .from("pm_raise_record rr join ce_chicks_ccs cc on rr.chicks_ccs_id = cc.id join cm_house h on cc.house_id = h.id join cm_farm f on h.farm_id = f.id")
                .where(where);
        generalDao.execute(query);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

    @Override
    public QueryResult queryMedicineOrVaccine(PreventionData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (StrFuncs.notEmpty(queryData.getRaiseTimeStart())) {
            where += " and rr.raise_time >= :raiseTimeStart";
            query.put("raiseTimeStart", queryData.getRaiseTimeStart());
        }
        if (StrFuncs.notEmpty(queryData.getRaiseTimeEnd())) {
            where += " and rr.raise_time <= :raiseTimeEnd";
            query.put("raiseTimeEnd", queryData.getRaiseTimeEnd());
        }
        if (queryData.getFarmId() != null) {
            where += " and f.id = :farmId";
            query.put("farmId", queryData.getFarmId());
        }
        if (queryData.getHouseId() != null) {
            where += " and h.id = :houseId";
            query.put("houseId", queryData.getHouseId());
        }
        if(queryData.getMedicineId() == null && queryData.getVaccineId() == null){
            where += " and (d.code = 1 or d.code = 2)";
        }else if(queryData.getMedicineId() != null && queryData.getVaccineId() != null){
            where += " and ((rr.raise_id = :medicineId and d.code = 1) or (rr.raise_id = :vaccineId and d.code = 2))";
            query.put("medicineId", queryData.getMedicineId());
            query.put("vaccineId", queryData.getVaccineId());
        }else if(queryData.getMedicineId() != null){
            where += " and rr.raise_id = :medicineId and d.code = 1";
            query.put("medicineId", queryData.getMedicineId());
        }else if(queryData.getVaccineId() != null){
            where += " and rr.raise_id = :vaccineId and d.code = 2";
            query.put("vaccineId", queryData.getVaccineId());
        }

        query.select("rr.id,f.id as farmId,h.id as houseId,rr.chicks_ccs_id as chicksCcsId,f.name as farmName,h.name as houseName,rr.raise_name as raiseName,rr.raise_value as raiseValue,rr.raise_time as raiseTime,d.name as type")
                .from("pm_raise_record rr join ce_chicks_ccs cc on rr.chicks_ccs_id = cc.id join cm_house h on cc.house_id = h.id join cm_farm f on h.farm_id = f.id join sys_dict d on rr.raise_type = d.id")
                .where(where)
                .orderBy("rr.create_time desc");
        generalDao.execute(query);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

    @Override
    public RaiseRecord getRaiseRecord(Integer id) {
        return raiseRecordDao.get(id);
    }

    @Override
    public void save(RaiseRecord raiseRecord) {
        raiseRecordDao.save(raiseRecord);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        raiseRecordDao.delete(id);
    }

    @Override
    public List<Farm> quertAllFarm() {
        return farmDao.getAll();
    }

    @Override
    public List<Feed> queryAllFeed() {
        return feedDao.getAll();
    }

    @Override
    public List<Medicine> queryAllMedicine() {
        return medicineDao.getAll();
    }

    @Override
    public List<Vaccine> queryAllVaccine() {
        return vaccineDao.getAll();
    }

    @Override
    public List<House> queryHouseByFarmId(Integer farmId) {
        String sql = "select id,name from cm_house where farm_id ="+farmId;
        List<House> houseList =  generalDao.queryList(House.class,sql);
        return houseList;
    }

    @Override
    public QueryResult queryChicksCcsByHouseId(Integer houseId) {
        Query query = new ListQuery();
        query.select("cc.id,c.batch_number as batchNumber,cc.create_time as createTime")
                .from("ce_chicks_ccs cc,cm_chicks c")
                .where(" cc.chicks_id = c.id and cc.house_id = "+houseId);
        generalDao.execute(query);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

    @Override
    public List<ChicksDisVo> getChicksDisIdWithBatchNum(Integer houseId) {
        String sql = "select cc.id,c.batch_number as batchNumber from ce_chicks_ccs cc,cm_chicks c where cc.chicks_id = c.id and cc.house_id = ?";
        return generalDao.queryList(ChicksDisVo.class,sql,houseId);
    }

    @Override
    public List<Dict> queryDict(String cat) {
       String sql = "select id,code,name from sys_dict where cat = ?";
       List<Dict> listDict = generalDao.queryList(Dict.class,sql,cat);
       return listDict;
    }

    @Override
    public Dict queryDict(Integer id) {
        String sql = "select id,code,name from sys_dict where id = ?";
        Dict dict = generalDao.queryValue(Dict.class,sql,id);
        return dict;
    }

    @Override
    public Chicks queryChicksByChicksCcsId(Integer chicksCcsId) {
        String sql = "select c.* from ce_chicks_ccs cc join cm_chicks c on cc.chicks_id = c.id where cc.id = ?";
        return generalDao.queryValue(Chicks.class,sql,chicksCcsId);
    }

}
