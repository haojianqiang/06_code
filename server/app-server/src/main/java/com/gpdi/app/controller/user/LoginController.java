package com.gpdi.app.controller.user;

import com.gpdi.app.data.UserData;
import com.gpdi.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.spring.web.mvc.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @RequestMapping("/user/login")
    public ActionResult login(String account,String password){
        UserData user = userService.login(account,password);
        if (user == null){
            return ActionResult.fail("账号密码错误!");
        }
        return ActionResult.okWithData(user);
    }
}
