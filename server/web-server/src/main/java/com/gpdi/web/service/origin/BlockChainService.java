package com.gpdi.web.service.origin;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.data.origin.BlockChain;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;

import java.util.List;

public interface BlockChainService {
    QueryResult query(BlockChain queryData, PageSettings settings);

    List<BlockChain> getSourceCodes();
}
