package com.gpdi.springcloudzuul.web.app.action;

import com.gpdi.springcloudzuul.data.UserWrapper;
import com.gpdi.springcloudzuul.entity.Nav;
import com.gpdi.springcloudzuul.entity.SysModule;
import com.gpdi.springcloudzuul.entity.SysUser;
import com.gpdi.springcloudzuul.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pub.spring.web.mvc.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author zzj
 * @date 2019/8/8 0008
 */
@Slf4j
@RestController
public class LoginAction {
//    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ActionResult login(HttpServletRequest request, String account, String password){
        // 客户端信息
        String ip = request.getRemoteAddr(); // IP
        int port = request.getRemotePort(); // 网络端口
        String remoteHost = request.getRemoteHost(); // 主机名
        // 日志记录
        log.info(String.format("[login] ip:%s ,port:%d ,hostname:%s ,account:%s",ip,port,remoteHost,account));
        SysUser user = loginService.login(account,password);
        if (user == null){
            return ActionResult.fail("账号,密码错误");
        }else{
            // 创建Session
            HttpSession session = request.getSession();
            session.setAttribute("UserId",user.getId());
            // 模块菜单以及导航栏菜单
            List<SysModule> modules = loginService.getModulesByUserId(user.getId());
            List<Nav> navs = loginService.getNavByModule(modules.get(0).getModuleId(),user.getNavId());
            UserWrapper userWrapper = new UserWrapper();
            userWrapper.setUserId(user.getId());
            userWrapper.setUserName(user.getUserName());
            userWrapper.setModules(modules);
            userWrapper.setNavs(navs);
            return ActionResult.ok("登陆成功!",userWrapper);
        }
    }

    // 注销
    @RequestMapping
    public ActionResult logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return ActionResult.ok("注销成功!");
    }
}
