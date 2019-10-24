package com.gpdi.springcloudzuul.data;

import com.gpdi.springcloudzuul.entity.Nav;
import com.gpdi.springcloudzuul.entity.SysModule;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息包装类
 */
@Data
public class UserWrapper implements Serializable{
    private Integer userId; // ID
    private String userName; // 用户名
    private List<SysModule> modules; // 模块菜单
    private List<Nav> navs; // 导航菜单,默认父级为首个模块菜单
}
