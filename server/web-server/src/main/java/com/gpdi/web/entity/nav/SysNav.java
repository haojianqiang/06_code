package com.gpdi.web.entity.nav;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 系统菜单
 */
@Entity
@Table(name = "t_nav")
public class SysNav implements Serializable{
    private Integer id;
    private String name; // 菜单名称
    private String info; // 备注信息
    private String url;  // 请求地址
    private String icon; // 图标
    private String navOrder; // 排名,值越小排越前
    private Integer parentId; // 父级菜单ID,0:一级菜单
    private Integer grandparentId; // 祖父级菜单ID
    private Integer greatgrandparentId; // 曾祖父级菜单ID

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "info")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name = "nav_order")
    public String getNavOrder() {
        return navOrder;
    }

    public void setNavOrder(String navOrder) {
        this.navOrder = navOrder;
    }

    @Column(name = "parent_id")
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Column(name = "grandparent_id")
    public Integer getGrandparentId() {
        return grandparentId;
    }

    public void setGrandparentId(Integer grandparentId) {
        this.grandparentId = grandparentId;
    }

    @Column(name = "greatgrandparent_id")
    public Integer getGreatgrandparentId() {
        return greatgrandparentId;
    }

    public void setGreatgrandparentId(Integer greatgrandparentId) {
        this.greatgrandparentId = greatgrandparentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysNav nav = (SysNav) o;
        if (id != null ? !id.equals(nav.id) : nav.id != null) return false;
        if (name != null ? !name.equals(nav.name) : nav.name != null) return false;
        if (info != null ? !info.equals(nav.info) : nav.info != null) return false;
        if (url != null ? !url.equals(nav.url) : nav.url != null) return false;
        if (icon != null ? !icon.equals(nav.icon) : nav.icon != null) return false;
        if (navOrder != null ? !navOrder.equals(nav.navOrder) : nav.navOrder != null) return false;
        if (parentId != null ? !parentId.equals(nav.parentId) : nav.parentId != null) return false;
        if (grandparentId != null ? !grandparentId.equals(nav.grandparentId) : nav.grandparentId != null) return false;
        return greatgrandparentId != null ? greatgrandparentId.equals(nav.greatgrandparentId) : nav.greatgrandparentId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (navOrder != null ? navOrder.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (grandparentId != null ? grandparentId.hashCode() : 0);
        result = 31 * result + (greatgrandparentId != null ? greatgrandparentId.hashCode() : 0);
        return result;
    }
}
