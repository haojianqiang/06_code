package com.gpdi.web.daoimpl.config.vaccine;

import com.gpdi.web.dao.config.vaccine.VaccineDao;
import com.gpdi.web.entity.config.vaccine.Vaccine;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class VaccineDaoImpl  extends MyBatisDao<Vaccine> implements VaccineDao {
    public VaccineDaoImpl (){
        super(Vaccine.class);
    };
}
