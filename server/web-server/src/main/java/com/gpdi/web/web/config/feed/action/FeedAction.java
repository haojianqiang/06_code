package com.gpdi.web.web.config.feed.action;

import com.gpdi.web.data.FeedData;
import com.gpdi.web.entity.config.feed.Feed;
import com.gpdi.web.service.config.feed.FeedService;
import com.gpdi.web.web.config.farm.action.QueryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;
import pub.spring.web.mvc.ActionResult;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
@RestController
public class FeedAction {
    private final static Logger logger = LoggerFactory.getLogger(QueryAction.class);

    @Autowired
    private FeedService feedService;

    @RequestMapping
    public QueryResult query(FeedData feedData, PageSettings settings){
        return feedService.query(feedData,settings);
    }

    @RequestMapping
    public ActionResult getFeed(Integer id) {
        Feed feed= feedService.getFeed(id);
        return ActionResult.okWithData(feed);
    }

    @RequestMapping
    public ActionResult save(Feed feed, HttpServletRequest request) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            String sDate = sdf.format(new Date());
            feed.setCreateTime(sdf.parse(sDate));
            feedService.save(feed);
            return ActionResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult delete(Integer[] ids) {
        try {
            for(Integer id: ids) {
                feedService.delete(id);
            }
            return ActionResult.ok();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }
}
