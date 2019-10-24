package com.gpdi.springcloudzuul.service;

import com.gpdi.springcloudzuul.entity.Nav;
import com.gpdi.springcloudzuul.entity.SysModule;
import com.gpdi.springcloudzuul.entity.SysUser;

import java.util.List;

/**
 * 登陆业务
 */
public interface LoginService {
    SysUser login(String account,String password);
    // 查询用户对应的模块菜单
    List<SysModule> getModulesByUserId(Integer userId);
    // 根据模块菜单以及用户权限查询导航菜单
    List<Nav> getNavByModule(Integer moduleId,String navId);
}
