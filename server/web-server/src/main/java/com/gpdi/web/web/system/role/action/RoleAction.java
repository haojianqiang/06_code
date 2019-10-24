package com.gpdi.web.web.system.role.action;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.role.SysRole;
import com.gpdi.web.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.dao.query.PageSettings;
import pub.dao.query.QueryResult;
import pub.spring.web.mvc.ActionResult;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RoleAction {
    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping
    public QueryResult query(QueryData queryData, PageSettings settings) {
        return sysRoleService.query(queryData, settings);
    }

    @RequestMapping
    public ActionResult delete(Integer[] ids) {
        try {
            for (Integer id : ids) {
                sysRoleService.delete(id);
            }
            return ActionResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

    @RequestMapping
    public ActionResult getModule(HttpServletRequest request) {
        String roleId = request.getParameter("roleId");
        ActionResult result = ActionResult.ok();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tree", sysRoleService.formTree(Integer.parseInt(roleId)));
        map.put("roleId", roleId);
        result.setData(map);
        return result;
    }

    @RequestMapping
    public ActionResult saveTree(HttpServletRequest request) {
        Integer roleId = Integer.valueOf(request.getParameter("rid"));
        String ids = request.getParameter("ids");
        String array[] = ids.replace("[", "").replace("]", "").split(",");
        ActionResult result = ActionResult.ok();
        sysRoleService.saveTree(roleId, array);
        return result;
    }

    @RequestMapping
    public ActionResult getRole(Integer roleId) {
        SysRole sysRole = sysRoleService.get(roleId);
        return ActionResult.okWithData(sysRole);
    }

    @RequestMapping
    public ActionResult save(SysRole sysRole) {
        try {
            if (sysRole.getIsEnable() == null) {
                sysRole.setIsEnable(0);
            }
            Integer id = sysRoleService.save(sysRole);
            return ActionResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResult.error(e);
        }
    }

}
