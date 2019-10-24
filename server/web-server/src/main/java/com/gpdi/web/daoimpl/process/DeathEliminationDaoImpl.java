package com.gpdi.web.daoimpl.process;

import com.gpdi.web.dao.process.DeathEliminationDao;
import com.gpdi.web.entity.process.DeathElimination;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;

@Repository
public class DeathEliminationDaoImpl extends MyBatisDao<DeathElimination> implements DeathEliminationDao {
    public DeathEliminationDaoImpl() {
        super(DeathElimination.class);
    }
}
