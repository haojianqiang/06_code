package com.gpdi.web.serviceimpl.config.vaccine;

import com.gpdi.web.dao.config.vaccine.VaccineDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.vaccine.Vaccine;
import com.gpdi.web.service.config.vaccine.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;
import pub.functions.StrFuncs;

@Service
@Transactional(readOnly = false)
public class VaccineServiceImpl implements VaccineService {

    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private VaccineDao vaccineDao;


    @Override
    public QueryResult query(QueryData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (StrFuncs.notEmpty(queryData.getName())) {
            where += " and v.name like :name";
            query.put("name", StrFuncs.anyLike(queryData.getName()));
        }
        if (StrFuncs.notEmpty(queryData.getProducer())) {
            where += " and v.producer like :producer";
            query.put("producer", StrFuncs.anyLike(queryData.getProducer()));
        }
        query.select("v.*")
                .from("cm_vaccine v")
                .where(where);
        generalDao.execute(query);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

    @Override
    public Vaccine getVaccine(Integer id) {
        return  vaccineDao.get(id);
    }

    @Override
    public void save(Vaccine vaccine) {
        vaccineDao.save(vaccine);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        vaccineDao.delete(id);
    }
}
