package com.gpdi.web.web.system.nav.action;

import com.gpdi.web.entity.nav.SysNav;
import com.gpdi.web.service.SysNavService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.spring.web.mvc.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class NavAction {
    private final static Logger logger = LoggerFactory.getLogger(NavAction.class);

    @Autowired
    private SysNavService navService;

    @RequestMapping
    public ActionResult queryByUserAndModule(HttpServletRequest request,Integer moduleId){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("UserId");
        if (userId != null && moduleId != null){
            List<SysNav> list = navService.queryByUserAndModule(moduleId,userId);
            return ActionResult.okWithData(list);
        }else{
            return ActionResult.error("请求失败!");
        }
    }

}
