package com.gpdi.springcloudzuul.filter;

import com.gpdi.springcloudzuul.utils.JsonUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import pub.spring.web.mvc.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登陆状态过滤器
 */
public class RedisSessionFilter extends ZuulFilter{
    private static final Logger logger = LoggerFactory.getLogger(RedisSessionFilter.class);

    @Override
    public String filterType() {
        return "pre"; // 在请求被路由之前执行
    }

    @Override
    public int filterOrder() {
        return 1; // 执行顺序
    }

    @Override
    public boolean shouldFilter() {
        return true; // 该过滤器是否需要被执行
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        HttpSession session = request.getSession();
//        logger.info("Filter Session Id："+session.getId());
        Object userId = session.getAttribute("UserId");
        if (userId != null){
            context.setSendZuulResponse(true); // 对该请求进行路由
            return null;
        }
        context.setSendZuulResponse(false); // 不对其进行路由
        response401(context.getResponse());
        return null;
    }

    // 用户未登陆
    private void response401(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().print(JsonUtils.objToJson(ActionResult.error("用户未登陆")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
