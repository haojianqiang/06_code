package com.gpdi.web.service.origin;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.data.origin.BlockChain;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;

public interface BlockChainService {
    QueryResult query(BlockChain queryData, PageSettings settings);
}
