package com.gpdi.web.serviceimpl.config.feed;

import com.gpdi.web.dao.config.feed.FeedDao;
import com.gpdi.web.data.FeedData;
import com.gpdi.web.entity.config.feed.Feed;
import com.gpdi.web.service.config.feed.FeedService;
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
public class FeedServiceImpl  implements FeedService {

    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private FeedDao feedDao;

    @Override
    public QueryResult query(FeedData feedData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (StrFuncs.notEmpty(feedData.getName())) {
            where += " and f.name like :name";
            query.put("name", StrFuncs.anyLike(feedData.getName()));
        }
        if (StrFuncs.notEmpty(feedData.getProducer())) {
            where += " and f.producer like :producer";
            query.put("producer", StrFuncs.anyLike(feedData.getProducer()));
        }
        query.select("f.*")
                .from("cm_feed f")
                .where(where);
        generalDao.execute(query);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

    @Override
    public Feed getFeed(Integer id) {
        return feedDao.get(id);
    }

    @Override
    public void save(Feed feed) {
        feedDao.save(feed);
    }

    @Override
    public void delete(Integer id) {
        feedDao.delete(id);
    }

}
