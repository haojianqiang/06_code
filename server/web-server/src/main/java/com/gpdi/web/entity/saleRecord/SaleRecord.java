package com.gpdi.web.entity.saleRecord;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//鸡成交信息
@Entity
@Table(name = "pm_sale_record")
public class SaleRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String assemblers;
    private Float price;
    private Integer count;
    private Float total;
    private Date saleTime;
    private Date createTime;
    private Date modifyTime;
    private String modifier;
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

    @Column(name = "assemblers")
    public String getAssemblers() {
        return assemblers;
    }

    public void setAssemblers(String assemblers) {
        this.assemblers = assemblers;
    }

    @Column(name = "price")
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Column(name = "count")
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Column(name = "total")
    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    @Column(name = "sale_time")
    public Date getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Date saleTime) {
        this.saleTime = saleTime;
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
