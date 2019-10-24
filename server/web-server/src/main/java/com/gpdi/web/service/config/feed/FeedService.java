package com.gpdi.web.service.config.feed;

import com.gpdi.web.data.FeedData;
import com.gpdi.web.entity.config.feed.Feed;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

public interface FeedService {
    QueryResult query(FeedData feedData, QuerySettings settings);

    Feed getFeed(Integer id);

    void save(Feed feed) ;

    void delete(Integer id);
}
