package com.gpdi.web.serviceimpl.config.chicks;

import com.gpdi.web.dao.config.chicks.ChicksDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.chicks.Chicks;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.vo.ChicksTransactionVo;
import com.gpdi.web.entity.vo.ProductionVo;
import com.gpdi.web.service.config.chicks.ChicksService;
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
public class ChicksServiceImpl implements ChicksService {
    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private ChicksDao chicksDao;

    @Override
    public QueryResult query(QueryData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (queryData.getBatchNumber() != null) {
            where += " and c.batch_number = :batchNumber";
            query.put("batchNumber", queryData.getBatchNumber());
        }
        if (StrFuncs.notEmpty(queryData.getChickType())) {
            where += " and c.chick_type like :chickType";
            query.put("chickType", StrFuncs.anyLike(queryData.getChickType()));
        }

        query.select("c.*,f.name as farmName")
                .from("cm_chicks c join cm_farm f on c.farm_id = f.id")
                .where(where)
                .orderBy("c.create_time desc");
        generalDao.execute(query);
        return query.getResult();
    }

    @Override
    public Chicks getChicks(Integer id) {
        return chicksDao.get(id);
    }

    @Override
    public void save(Chicks chicks) {
        chicksDao.save(chicks);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        chicksDao.delete(id);
    }

    @Override
    public List<Farm> getFarmNameAndId() {
        return generalDao.queryList(Farm.class, "SELECT f.id, f.name FROM `cm_farm` f");
    }

    @Override
    public List<ChicksTransactionVo> getTransactionInfoByHouseid(Integer houseid) {
        return generalDao.queryList(ChicksTransactionVo.class,"select cm.chick_type ,sum(ce.chick_count) as chickcount ,pe.sale_count from cm_chicks cm left join ce_chicks_ccs ce on cm.id=ce.chicks_id left JOIN pe_sale_record_ccs pe on ce.house_id=pe.house_id where  ce.house_id=? group by cm.chick_type",houseid);
    }

    @Override
    public List<ProductionVo> getProductionInfoByDate(String startTime, String endTime) {
        List<ProductionVo> productionVos = generalDao.queryList(ProductionVo.class, "select  date_format(sale_time,'%Y-%m') as sale_time,sum(total) as total,sum(count) as count from pm_sale_record where sale_time between ? and ? group by date_format(sale_time, '%Y-%m')", startTime, endTime);
        return productionVos;
    }
}
