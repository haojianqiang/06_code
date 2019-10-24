package com.gpdi.web.entity.roleModule;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 角色模块关联表
 */
@Entity
@Table(name = "sys_role_module")
public class SysRoleModule implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer roleId;
    private Integer moduleId;
    private Integer isEnable;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "role_id")
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Column(name = "module_id")
    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    @Column(name = "is_enable")
    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }
}
