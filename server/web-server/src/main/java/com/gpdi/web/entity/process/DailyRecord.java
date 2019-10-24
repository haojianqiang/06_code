package com.gpdi.web.entity.process;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pm_daily_record")
public class DailyRecord implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer chicksCcsId;
    private Integer avgWeight;
    private Integer water;
    private String  hygiene;
    private String  remark;
    private Date  createTime;
    private Date    modifyTime;
    private String  modifier;
    private Integer modifierId;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "chicks_ccs_id")
    public Integer getChicksCcsId() {
        return chicksCcsId;
    }

    public void setChicksCcsId(Integer chicksCcsId) {
        this.chicksCcsId = chicksCcsId;
    }

    @Column(name = "avg_weight")
    public Integer getAvgWeight() {
        return avgWeight;
    }

    public void setAvgWeight(Integer avgWeight) {
        this.avgWeight = avgWeight;
    }

    @Column(name = "water")
    public Integer getWater() {
        return water;
    }

    public void setWater(Integer water) {
        this.water = water;
    }

    @Column(name = "hygiene")
    public String getHygiene() {
        return hygiene;
    }

    public void setHygiene(String hygiene) {
        this.hygiene = hygiene;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "modify_time")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(name = "modifier")
    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Column(name = "modifier_id")
    public Integer getModifierId() {
        return modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }
}
