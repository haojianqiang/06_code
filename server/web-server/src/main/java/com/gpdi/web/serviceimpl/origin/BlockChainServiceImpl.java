package com.gpdi.web.serviceimpl.origin;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.data.origin.BlockChain;
import com.gpdi.web.service.origin.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.PageSettings;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class BlockChainServiceImpl implements BlockChainService {
    @Autowired
    private GeneralDao generalDao;

    public QueryResult query(BlockChain blockChain, PageSettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (blockChain.getSourceCode() != null) {
            where += " and s.source_code = :code";
            query.put("code", blockChain.getSourceCode());
        }
        where += " and s.house_id = h.id and h.farm_id = f.id";
        query.select("s.source_code,s.hash,f.county,f.town,f.name farm,h.name house")
                .from("og_source s,cm_farm f,cm_house h")
                .where(where);
        generalDao.execute(query);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

    @Override
    public List<BlockChain> getSourceCodes() {
        return generalDao.queryList(BlockChain.class, "SELECT f.id, f.source_code FROM `og_source` f order by createTime desc limit 5");
    }
}
