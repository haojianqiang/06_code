package com.gpdi.web.entity.process;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pm_raise_record")
public class RaiseRecord implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer chicksCcsId;
    private Integer raiseType;
    private Integer raiseId;
    private String  raiseName;
    private Double  raiseValue;
    private String  raiseUnit;
    private String raiseReason;
    private Date  raiseTime;
    private Integer chickDayAge;
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

    @Column(name = "raise_type")
    public Integer getRaiseType() {
        return raiseType;
    }

    public void setRaiseType(Integer raiseType) {
        this.raiseType = raiseType;
    }

    @Column(name = "raise_id")
    public Integer getRaiseId() {
        return raiseId;
    }

    public void setRaiseId(Integer raiseId) {
        this.raiseId = raiseId;
    }

    @Column(name = "raise_name")
    public String getRaiseName() {
        return raiseName;
    }

    public void setRaiseName(String raiseName) {
        this.raiseName = raiseName;
    }

    @Column(name = "raise_value")
    public Double getRaiseValue() {
        return raiseValue;
    }

    public void setRaiseValue(Double raiseValue) {
        this.raiseValue = raiseValue;
    }

    @Column(name = "raise_unit")
    public String getRaiseUnit() {
        return raiseUnit;
    }

    public void setRaiseUnit(String raiseUnit) {
        this.raiseUnit = raiseUnit;
    }

    @Column(name = "raise_reason")
    public String getRaiseReason() {
        return raiseReason;
    }

    public void setRaiseReason(String raiseReason) {
        this.raiseReason = raiseReason;
    }

    @Column(name = "raise_time")
    public Date getRaiseTime() {
        return raiseTime;
    }

    public void setRaiseTime(Date raiseTime) {
        this.raiseTime = raiseTime;
    }

    @Column(name = "chick_day_age")
    public Integer getChickDayAge() {
        return chickDayAge;
    }

    public void setChickDayAge(Integer chickDayAge) {
        this.chickDayAge = chickDayAge;
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
