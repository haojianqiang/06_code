package com.gpdi.web.serviceimpl;

import com.gpdi.web.entity.nav.SysNav;
import com.gpdi.web.service.SysNavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.dao.GeneralDao;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class SysNavServiceImpl implements SysNavService {
    @Autowired
    private GeneralDao generalDao;

    @Override
    public List<SysNav> queryByUserAndModule(Integer moduleId, Integer userId) {
        List<SysNav> navs = new ArrayList<>();
        // 查询用户导航菜单权限
        String userSql = "select nav_id from sys_user where id = " + userId;
        String navId = generalDao.queryValue(String.class,userSql);

        // 根据模块查询所有的菜单
        String sql = "select * from t_nav where id in (select nav_id from sys_nav_module where module_id = ?)";
        List<SysNav> moduleNavs = generalDao.queryList(SysNav.class,sql,moduleId);
        List<SysNav> AuthNavs = new ArrayList<>();

        if(navId.equals("0")){ // 超级管理员,查看所有模块
            // 用户支持的菜单
            AuthNavs = generalDao.queryList(SysNav.class,"select * from t_nav");
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
                List<SysNav> newNavs = generalDao.queryList(SysNav.class, sql2, id, id, id, id, id, id, id);
                AuthNavs.removeAll(newNavs);
                AuthNavs.addAll(newNavs);
            }
        }

        for (SysNav nav:moduleNavs){
            if (AuthNavs.contains(nav)){
                navs.add(nav);
            }
        }
        return navs;
    }
}
