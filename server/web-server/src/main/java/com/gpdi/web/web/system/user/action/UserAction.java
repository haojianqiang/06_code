package com.gpdi.web.web.system.user.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.user.SysUser;
import com.gpdi.web.service.SysRoleService;
import com.gpdi.web.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;
import pub.spring.web.mvc.ActionResult;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserAction {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleService roleService;

    @RequestMapping
    public QueryResult query(QueryData queryData, PageSettings settings){
        return userService.query(queryData,settings);
    }

    @RequestMapping
    public ActionResult list(){
        return ActionResult.okWithData(userService.list());
    }

    @RequestMapping
    public ActionResult save(SysUser sysUser) {
        try {
            userService.save(sysUser);
            return ActionResult.ok();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult delete(Integer[] ids) {
        try {
            for(Integer id: ids) {
                userService.delete(id);
            }
            return ActionResult.ok();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult getUser(Integer userId) {
        SysUser user= userService.getUser(userId);
        return ActionResult.okWithData(user);
    }

    @RequestMapping
    public ActionResult getMenuTree(HttpServletRequest request) {
        String userId=request.getParameter("userId");
        ActionResult result=ActionResult.ok();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("tree", userService.formMenuTree(Integer.parseInt(userId)));
        map.put("userId", userId);
        result.setData(map);
        return result;
    }

    @RequestMapping
    public ActionResult getDeptTree(HttpServletRequest request) {
        String userId=request.getParameter("userId");
        ActionResult result=ActionResult.ok();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("tree", userService.formDeptTree(Integer.parseInt(userId)));
        map.put("userId", userId);
        result.setData(map);
        return result;
    }

    @RequestMapping
    public ActionResult getRoleTree(HttpServletRequest request) {
        String userId=request.getParameter("userId");
        ActionResult result=ActionResult.ok();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("tree", userService.formRoleTree(Integer.parseInt(userId)));
        map.put("userId", userId);
        result.setData(map);
        return result;
    }

    @RequestMapping
    public ActionResult saveMenuTree(HttpServletRequest request) throws ParseException {
        Integer userid = Integer.valueOf(request.getParameter("uid"));
        String ids = request.getParameter("ids");
        String navs = ids.replace("[","").replace("]","");
        userService.updateNav(userid,navs);
        ActionResult result=ActionResult.ok();
        return result;
    }

    @RequestMapping
    public ActionResult saveDeptTree(HttpServletRequest request) throws ParseException {
        Integer userid = Integer.valueOf(request.getParameter("uid"));
        String ids = request.getParameter("ids");
        String func = ids.replace("[","").replace("]","");
        SysUser sysUser = userService.getUser(userid);
        sysUser.setFuncId(func);
        userService.save(sysUser);
        ActionResult result=ActionResult.ok();
        return  result;
    }

    @RequestMapping
    public ActionResult saveDeptTree2(HttpServletRequest request) throws ParseException {
        Integer userid = Integer.valueOf(request.getParameter("uid"));
        String ids = request.getParameter("ids");
        String func = ids.replace("[","").replace("]","");
        userService.updateFunc(userid,func);
        ActionResult result=ActionResult.ok();
        return  result;
    }

    @RequestMapping
    public ActionResult saveRoleTree(HttpServletRequest request) throws ParseException {
        Integer userid = Integer.valueOf(request.getParameter("uid"));
        String ids = request.getParameter("ids");
        String func = ids.replace("[","").replace("]","");
        roleService.updateRole(userid,func.split(","));
        ActionResult result=ActionResult.ok();
        return  result;
    }

}
