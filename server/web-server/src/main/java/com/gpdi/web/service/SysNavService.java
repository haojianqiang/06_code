package com.gpdi.web.service;


import com.gpdi.web.entity.nav.SysNav;

import java.util.List;

public interface SysNavService {
    // 根据用户以及模块查询相关导航菜单
    List<SysNav> queryByUserAndModule(Integer moduleId, Integer userId);
}
