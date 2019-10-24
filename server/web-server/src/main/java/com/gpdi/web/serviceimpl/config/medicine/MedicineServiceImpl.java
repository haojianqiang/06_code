package com.gpdi.web.serviceimpl.config.medicine;


import com.gpdi.web.dao.config.medicine.MedicineDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.config.medicine.Medicine;
import com.gpdi.web.service.config.medicine.MedicineService;
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
public class MedicineServiceImpl  implements MedicineService {

    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private MedicineDao medicineDao;


    @Override
    public QueryResult query(QueryData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (StrFuncs.notEmpty(queryData.getName())) {
            where += " and m.name like :name";
            query.put("name", StrFuncs.anyLike(queryData.getName()));
        }
        if (StrFuncs.notEmpty(queryData.getProducer())) {
            where += " and m.producer like :producer";
            query.put("producer", StrFuncs.anyLike(queryData.getProducer()));
        }
         query.select("m.*")
                .from("cm_medicine m")
                .where(where);
        generalDao.execute(query);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

    @Override
    public Medicine getMedicine(Integer id) {
        return  medicineDao.get(id);
    }

    @Override
    public void save(Medicine medicine) {
        medicineDao.save(medicine);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        medicineDao.delete(id);
    }
}
