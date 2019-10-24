package com.gpdi.springcloudzuul.serviceImpl;

import com.gpdi.springcloudzuul.dao.SysUserDao;
import com.gpdi.springcloudzuul.entity.Nav;
import com.gpdi.springcloudzuul.entity.SysModule;
import com.gpdi.springcloudzuul.entity.SysUser;
import com.gpdi.springcloudzuul.service.LoginService;
import com.gpdi.springcloudzuul.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pub.dao.GeneralDao;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    private SysUserDao userDao;
    @Autowired
    private GeneralDao generalDao;

    @Override
    public SysUser login(String account, String password) {
        SysUser user = userDao.getUser(account);
        if (user == null || !user.getPwd().equals(MD5Util.MD5(password))){
            return null;
        }
        return user;
    }

    @Override
    public List<SysModule> getModulesByUserId(Integer userId) {
        String sql ="select * from sys_module where module_id in " +
                "(select module_id from sys_role_module where role_id in (select role_id from sys_role_user where user_id = ? and is_enable = 0) and is_enable = 0) order by module_id asc";
        List<SysModule> list = generalDao.queryList(SysModule.class,sql,userId);
        return list;
    }

    @Override
    public List<Nav> getNavByModule(Integer moduleId, String navId) {
        List<Nav> navs = new ArrayList<>();
        // 根据模块查询所有的菜单
        String sql = "select * from t_nav where id in (select nav_id from sys_nav_module where module_id = ?)";
        List<Nav> moduleNavs = generalDao.queryList(Nav.class,sql,moduleId);
        List<Nav> AuthNavs = new ArrayList<>();
        if(navId.equals("0")){ // 超级管理员,查看所有模块
            // 用户支持的菜单
            AuthNavs = generalDao.queryList(Nav.class,"select * from t_nav");
        } else {
            for (String id : navId.split(",")) {
                String sql2 = "select n1.* from t_nav n1 JOIN t_nav n2 on n1.id = n2.parent_id where n2.id = ? " +  //曾祖父菜单
                        "UNION\n" +
                        "select n1.* from t_nav n1 JOIN t_nav n2 on n1.id = n2.grandparent_id where n2.id = ? "  +  //祖父菜单
                        "UNION\n" +
                        "select n1.* from t_nav n1 JOIN t_nav n2 on n1.id = n2.greatgrandparent_id where n2.id = ? " + //父菜单
                        "UNION\n" +
                        "select * from t_nav where id = ? " + //本身
                        "UNION\n" +
                        "select n1.* from t_nav n1 JOIN t_nav n2 on n1.parent_id = n2.id where n2.id = ? "+  //子菜单
                        "UNION\n" +
                        "select n1.* from t_nav n1 JOIN t_nav n2 on n1.grandparent_id = n2.id where n2.id = ?"+  //孙菜单
                        "UNION\n" +
                        "select n1.* from t_nav n1 JOIN t_nav n2 on n1.greatgrandparent_id = n2.id where n2.id = ?";  //曾孙菜单
                List<Nav> newNavs = generalDao.queryList(Nav.class, sql2, id, id, id, id, id, id, id);
                AuthNavs.removeAll(newNavs);
                AuthNavs.addAll(newNavs);
            }
        }

        for (Nav nav:moduleNavs){
            if (AuthNavs.contains(nav)){
                navs.add(nav);
            }
        }
        return navs;
    }
}
