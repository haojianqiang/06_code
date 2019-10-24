package com.gpdi.web.serviceimpl.config.farm;

import com.gpdi.web.dao.SysRoleDao;
import com.gpdi.web.dao.SysUserDao;
import com.gpdi.web.dao.config.farm.FarmDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.Dept;
import com.gpdi.web.entity.Nav;
import com.gpdi.web.entity.config.farm.Farm;
import com.gpdi.web.entity.role.SysRole;
import com.gpdi.web.entity.user.SysUser;
import com.gpdi.web.service.SysUserService;
import com.gpdi.web.service.config.farm.FarmService;
import com.gpdi.web.utils.MD5Util;
import com.gpdi.web.utils.TypeUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;
import pub.functions.StrFuncs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class FarmServiceImpl implements FarmService {
    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private FarmDao farmDao;


    @Override
    public QueryResult query(QueryData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (StrFuncs.notEmpty(queryData.getName())) {
            where += " and f.name like :name";
            query.put("name", StrFuncs.anyLike(queryData.getName()));
        }
        if (StrFuncs.notEmpty(queryData.getCharger())) {
            where += " and f.charger like :charger";
            query.put("charger", StrFuncs.anyLike(queryData.getCharger()));
        }
        if (StrFuncs.notEmpty(queryData.getPhoneNumber())) {
            where += " and f.phone_number like :phoneNumber";
            query.put("phoneNumber", queryData.getPhoneNumber());
        }
        if (StrFuncs.notEmpty(queryData.getAddress())) {
            where += " and f.address like :address";
            query.put("address",  StrFuncs.anyLike(queryData.getAddress()));
        }
        query.select("f.*")
                .from("cm_farm f")
                .where(where)
                .orderBy("f.create_time desc");
        generalDao.execute(query);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

    @Override
    public Farm getFarm(Integer id) {
       return farmDao.get(id);
    }

    @Override
    public void save(Farm farm) {
        farmDao.save(farm);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        farmDao.delete(id);
    }

}
