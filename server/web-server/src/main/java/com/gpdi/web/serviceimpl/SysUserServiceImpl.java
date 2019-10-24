package com.gpdi.web.serviceimpl;

import com.gpdi.web.dao.SysRoleDao;
import com.gpdi.web.dao.SysUserDao;
import com.gpdi.web.data.QueryData;
import com.gpdi.web.entity.Dept;
import com.gpdi.web.entity.Nav;
import com.gpdi.web.entity.role.SysRole;
import com.gpdi.web.entity.user.SysUser;
import com.gpdi.web.service.SysUserService;
import com.gpdi.web.utils.MD5Util;
import com.gpdi.web.utils.TypeUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;
import pub.dao.jdbc.dialect.mysql.PagedQuery;
import pub.dao.query.Query;
import pub.dao.query.QueryResult;
import pub.dao.query.QuerySettings;
import pub.functions.StrFuncs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private SysUserDao userDao;
    @Autowired
    private SysRoleDao roleDao;

    @Override
    public QueryResult query(QueryData queryData, QuerySettings settings) {
        Query query = new PagedQuery<>(settings);
        String where = "";
        if (StrFuncs.notEmpty(queryData.getName())) {
            where += " and i.user_name like :name";
            query.put("name", StrFuncs.anyLike(queryData.getName()));
        }
        query.select("i.*")
                .from("sys_user i")
                .where(where);
        QueryResult queryResult = query.getResult();
        return queryResult;
    }

    @Override
    public Integer save(SysUser sysUser) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (sysUser.getId() == null){
            sysUser.setPwdLastModtime(new Date());
            sysUser.setPwd(MD5Util.MD5(sysUser.getPwd()));
            sysUser.setIsEnable(1);
            userDao.save(sysUser);
        }else{
            String sql = "update sys_user set user_name = '"+sysUser.getUserName()+"',account = '"+sysUser.getAccount()+"' where id = "+sysUser.getId();
            generalDao.execute(sql);
        }
        return sysUser.getId();
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        userDao.delete(id);
        String sql="delete from sys_role_user where user_id="+id;
        generalDao.execute(sql);
    }

    @Override
    public List<SysUser> list() {
        String sql = "select * from sys_user";
        return generalDao.queryList(SysUser.class,sql);
    }

    @Override
    public SysUser getUser(Integer id) {
        return userDao.getUser(id);
    }

    @Override
    public String formMenuTree(Integer userid) {
        String sql = "SELECT * FROM t_nav";
        List<Nav> navs =  generalDao.queryList(Nav.class, sql);
        String userSql = "SELECT nav_id FROM sys_user WHERE id = ?";
        String navStr = generalDao.queryValue(String.class, userSql, userid);
        List<String> navList = TypeUtils.strToList(navStr);
        JSONArray jsonArray = new JSONArray();
        for (Nav nav: navs) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", nav.getId());
            jsonObject.put("pId",
                    nav.getGreatgrandparentId()==null?(nav.getGrandparentId()==null?nav.getParentId():nav.getGrandparentId()):nav.getGreatgrandparentId());
            jsonObject.put("name", nav.getName());
            if(navStr!=null && navStr.equals("0")){
                jsonObject.put("checked", true);
            } else if(navList.contains(String.valueOf(nav.getId()))
                    || navList.contains(String.valueOf(nav.getParentId()))
                    || navList.contains(String.valueOf(nav.getGrandparentId()))
                    || navList.contains(String.valueOf(nav.getGreatgrandparentId()))){
                jsonObject.put("checked", true);
            } else {
                jsonObject.put("checked", false);
            }
            if(nav.getParentId() == 0) {
                jsonObject.put("open", true);
            } else {
                jsonObject.put("open", false);
            }
            jsonArray.add(jsonObject);
        }
        return toArray(jsonArray);
    }

    @Override
    public String formDeptTree(Integer userid) {
        String sql = "SELECT * FROM t_dept";
        List<Dept> depts =  generalDao.queryList(Dept.class, sql);
        String userSql = "SELECT func_id FROM sys_user WHERE id = ?";
        String funcStr = generalDao.queryValue(String.class, userSql, userid);
        List<String> funcList = TypeUtils.strToList(funcStr);
        JSONArray jsonArray = new JSONArray();
        List<Integer> checkDepts = new ArrayList<>();
        for (Dept dept: depts) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", dept.getId());
            jsonObject.put("pId", dept.getParentId());
            jsonObject.put("name", dept.getName());
            if(funcStr != null && funcStr.equals("0")){
                jsonObject.put("checked", true);
            } else if(funcList.contains(String.valueOf(dept.getId()))
                    || checkDepts.contains(dept.getParentId())){
                checkDepts.add(dept.getId());
                jsonObject.put("checked", true);
            } else {
                jsonObject.put("checked", false);
            }
            if(dept.getParentId()==null) {
                jsonObject.put("open", true);
            } else {
                jsonObject.put("open", false);
            }
            jsonArray.add(jsonObject);
        }
        return toArray(jsonArray);
    }

    @Override
    public String formRoleTree(Integer userid) {
        String sql = "SELECT * FROM sys_role";
        List<SysRole> roles =  generalDao.queryList(SysRole.class, sql);
        String userSql = "SELECT role_id FROM sys_role_user WHERE user_id = ? and is_enable = 0";
        List<Integer> roleIds = generalDao.queryList(Integer.class, userSql, userid);
        JSONArray jsonArray = new JSONArray();
        List<Integer> checkRoles = new ArrayList<>();
        for (SysRole role:roles) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", role.getRoleId());
            jsonObject.put("pId", 0);
            jsonObject.put("name", role.getRoleName());
            if(roleIds.contains(role.getRoleId())){
                checkRoles.add(role.getRoleId());
                jsonObject.put("checked", true);
            } else {
                jsonObject.put("checked", false);
            }

            jsonObject.put("open", false);
            jsonArray.add(jsonObject);
        }
        return toArray(jsonArray);
    }

    @Override
    public boolean updateNav(Integer userid, String navs) {
        String sql = "UPDATE sys_user SET nav_id = ? WHERE id = ?";
        generalDao.execute(sql, navs, userid);
        return true;
    }

    @Override
    public boolean updateFunc(Integer userid, String funcs) {
        String sql = "UPDATE sys_user SET func_id = ? WHERE id = ?";
        generalDao.execute(sql, funcs, userid);
        return true;
    }

    private String toArray(JSONArray jsonArray) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.getString(i));
        }
        String[] stringArray = list.toArray(new String[list.size()]);
        StringBuilder sb = new StringBuilder("[");
        int len = stringArray.length;
        for (int a = 0; a < len; a++) {
            sb.append(stringArray[a]).append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
