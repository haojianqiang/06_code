package com.gpdi.web.entity.saleRecord;

import javax.persistence.*;
import java.io.Serializable;

//在某个成交记录中每个鸡舍的成交信息
@Entity
@Table(name = "pe_sale_record_ccs")
public class SaleRecordCcs  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer parentId;
    private Integer houseId;
    private Integer chickCcsId;
    private Integer saleCount;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "parent_id")
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Column(name = "house_id")
    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    @Column(name = "chick_ccs_id")
    public Integer getChickCcsId() {
        return chickCcsId;
    }

    public void setChickCcsId(Integer chickCcsId) {
        this.chickCcsId = chickCcsId;
    }

    @Column(name = "sale_count")
    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }
}
