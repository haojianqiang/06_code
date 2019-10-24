package com.gpdi.web.entity;

import javax.persistence.*;

/**
 * @description:
 * @author: wck
 * @data: Created in 2019.3.11
 * 部分类
 * @modifier:
 */
@Entity
@Table(name = "t_dept")
public class Dept {
    private Integer id;
    private String name;//部门名
    private String allName;//部门全名
    private Integer parentId;//父级id
    private String insertTime; //更新时间
    private Integer level; //等级

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    @Column(name = "parent_id")
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Column(name = "all_name")
    public String getAllName() {
        return allName;
    }

    public void setAllName(String allName) {
        this.allName = allName;
    }

    @Column(name = "insert_time")
    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    @Column(name = "level")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
