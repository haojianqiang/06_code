package com.gpdi.web.web.origin.blockchain.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.data.origin.BlockChain;
import com.gpdi.web.service.origin.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;
import pub.spring.web.mvc.ActionResult;

import java.util.List;
import java.util.Map;

@RestController
public class QueryAction {

    @Autowired
    private BlockChainService blockChainService;

    @RequestMapping
    public QueryResult query(BlockChain queryData, PageSettings settings){
        QueryResult result = blockChainService.query(queryData,settings);
        List<Map<String, Object>> list = result.getRows();
        result.setRows(list);
        return result;
    }

    @RequestMapping
    public ActionResult getSourceCodes(){
        List<BlockChain> farms = blockChainService.getSourceCodes();
        return ActionResult.okWithData(farms);
    }
}
