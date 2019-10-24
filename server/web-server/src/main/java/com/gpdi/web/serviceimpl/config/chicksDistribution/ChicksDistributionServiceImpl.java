package com.gpdi.web.serviceimpl.config.chicksDistribution;

import com.gpdi.web.dao.config.chicksDistribution.ChicksDistributionDao;
import com.gpdi.web.data.ChicksDistributionData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.chicksDistribution.ChicksDistribution;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.config.house.House;
import com.gpdi.web.entity.vo.HouseVo;
import com.gpdi.web.service.config.chicksDistribution.ChicksDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;
import pub.functions.StrFuncs;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class ChicksDistributionServiceImpl implements ChicksDistributionService {
    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private ChicksDistributionDao chicksDistributionDao;

    @Override
    public QueryResult query(ChicksDistributionData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if ((queryData.getFarmId()!=null)) {
            where += " and f.id = :farmId";
            query.put("farmId",queryData.getFarmId());
        }
        if (queryData.getHouseId()!=null) {
            where += " and cc.house_id = :houseId";
            query.put("houseId", queryData.getHouseId());
        }
        if (queryData.getProduceCycle()!=null) {
            where += " and cc.produce_cycle = :produceCycle";
            query.put("produceCycle", queryData.getProduceCycle());
        }
        if (queryData.getChicksId()!=null) {
            where += " and cc.chicks_id = :chicksId";
            query.put("chicksId", queryData.getChicksId());
        }
        if (StrFuncs.notEmpty(queryData.getChickType())) {
            where += " and c.chick_type like :chickType";
            query.put("chickType", queryData.getChickType());
        }

        query.select("cc.id,cc.chick_count as chickCount,cc.produce_cycle as produceCycle,cc.origin_code as originCode,cc.qr_code as qrCode,cc.create_time as createTime,cc.modifier,f.id as farmId,f.name as farmName,cc.house_id as houseId,h.name as houseName,c.id as chicksId,c.batch_number as batchNumber,c.chick_type chickType,c.producer,c.produce_time as produceTime,cc.status")
                .from("ce_chicks_ccs cc join cm_house h on cc.house_id = h.id join cm_farm f on h.farm_id = f.id join cm_chicks c on cc.chicks_id = c.id")
                .where(where)
                .orderBy("cc.create_time desc");
        generalDao.execute(query);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

    @Override
    public ChicksDistribution getChicksDistribution(Integer id) {
        return chicksDistributionDao.get(id);
    }

    @Override
    public void save(ChicksDistribution chicksDistribution) {
        chicksDistributionDao.save(chicksDistribution);
    }

    @Override
    public void delete(Integer id) {
        chicksDistributionDao.delete(id);
    }

    @Override
    public List<Farm> getFarmNameAndId() {
        return generalDao.queryList(Farm.class, "SELECT f.id, f.name FROM `cm_farm` f");
    }

    @Override
    public List<House> getHouseNameAndId(Integer farmId) {
        String sql = "SELECT h.id, h.name FROM `cm_house` h WHERE h.farm_id = ?";
        return generalDao.queryList(House.class,sql,farmId);
    }

    @Override
    public List<Chicks> getChicksBatchNumAndId(Integer farmId) {
        String sql = "SELECT c.id, c.batch_number,c.unallocated_capacity FROM `cm_chicks` c WHERE c.farm_id = ?";
        return generalDao.queryList(Chicks.class,sql,farmId);
    }

    @Override
    public List<Chicks> getChicksBatchNumByHouseId(Integer houseId) {
        String sql = "SELECT c.id, c.batch_number AS batchNumber FROM `cm_chicks` c WHERE c.farm_id = (SELECT farm_id FROM cm_house WHERE id = ? limit 1)";
        return generalDao.queryList(Chicks.class,sql,houseId);
    }

    @Override
    public List<House> getHouseNameAndIdByFarmId(Integer farmId) {
        String sql = "select id,name from cm_house where farm_id ="+farmId;
        List<House> houseList =  generalDao.queryList(House.class,sql);
        return houseList;
    }

    @Override
    public List<Chicks> getChicksBatchNumAndTypeAndProducerAndId() {
        return generalDao.queryList(Chicks.class, "SELECT c.id, c.batch_number,chick_count FROM `cm_chicks` c");
    }

    @Override
    public ChicksDistribution queryChicksDistribution(ChicksDistribution chicksDistribution) {
        String sql = "select * from ce_chicks_ccs where house_id = ? and chicks_id = ?";
        return generalDao.queryValue(ChicksDistribution.class,sql,chicksDistribution.getHouseId(),chicksDistribution.getChicksId());
    }

    @Override
    public List<HouseVo> getUnDistributionHouse(Integer farmId) {
        String sql = "select h.id,h.name,h.capacity,'0' as stock from cm_house h where h.id not in (select cc.house_id from ce_chicks_ccs cc where cc.status = 1) and h.farm_id = ?";
        return generalDao.queryList(HouseVo.class,sql,farmId);
    }

    @Override
    public HouseVo getDistributionHouse(Integer chicksDistributionId) {
        String sql = "select cc.id as chicksDistributionId,h.id,h.name,h.capacity,cc.chick_count as stock from cm_house h join ce_chicks_ccs cc on cc.house_id = h.id where cc.id = ?";
        return generalDao.queryValue(HouseVo.class,sql,chicksDistributionId);
    }

    @Override
    public List<ChicksDistribution> getChick_countByHouseid(Integer Houseid) {
        return generalDao.queryList(ChicksDistribution.class,"select chick_count from ce_chicks_ccs where house_id=?",Houseid);
    }
}
