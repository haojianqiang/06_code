package com.gpdi.web.entity.config.chicks;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cm_chicks")
public class Chicks implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer farmId;
    private String batchNumber;
    private String  chickType;
    private Integer chickCount;
    private String  producer;
    private Integer  unallocatedCapacity;
    private String  unit;
    private Float   price;
    private Date    produceTime;
    private Date    createTime;
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

    @Column(name = "farm_id")
    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
    }

    @Column(name = "batch_number")
    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    @Column(name = "chick_type")
    public String getChickType() {
        return chickType;
    }

    public void setChickType(String chickType) {
        this.chickType = chickType;
    }

    @Column(name = "chick_count")
    public Integer getChickCount() {
        return chickCount;
    }

    public void setChickCount(Integer chickCount) {
        this.chickCount = chickCount;
    }

    @Column(name = "producer")
    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Column(name = "unallocated_capacity")
    public Integer getUnallocatedCapacity() {
        return unallocatedCapacity;
    }

    public void setUnallocatedCapacity(Integer unallocatedCapacity) {
        this.unallocatedCapacity = unallocatedCapacity;
    }

    @Column(name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column(name = "price")
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Column(name = "produce_time")
    public Date getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(Date produceTime) {
        this.produceTime = produceTime;
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
