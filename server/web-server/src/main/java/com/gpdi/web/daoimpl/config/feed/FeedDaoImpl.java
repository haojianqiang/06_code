package com.gpdi.web.daoimpl.config.feed;

import com.gpdi.web.dao.config.feed.FeedDao;
import com.gpdi.web.entity.config.feed.Feed;
import org.springframework.stereotype.Repository;
import pub.dao.mybatis.MyBatisDao;
@Repository
public class FeedDaoImpl extends MyBatisDao<Feed> implements FeedDao {
    public FeedDaoImpl() {
        super(Feed.class);
    }
}
