package com.gpdi.web.daoimpl.config.medicine;

import com.gpdi.web.dao.config.medicine.MedicineDao;
import com.gpdi.web.entity.config.medicine.Medicine;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class MedicineDaoImpl extends MyBatisDao<Medicine> implements MedicineDao {
     public MedicineDaoImpl (){
         super(Medicine.class);
     }
}
