package com.gpdi.web.service;

import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.user.SysUser;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;

import java.text.ParseException;
import java.util.List;

/**
 * 用户业务类
 */
public interface SysUserService {

    QueryResult query(QueryData queryData, QuerySettings settings);

    List<SysUser> list();

    Integer save(SysUser sysUser) throws ParseException;

    void delete(Integer id);

    SysUser getUser(Integer id);

    String formMenuTree(Integer userid);

    String formDeptTree(Integer userid);

    String formRoleTree(Integer userid);

    boolean updateNav(Integer userid, String navs);

    boolean updateFunc(Integer userid, String funcs);
}
