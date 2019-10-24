package com.gpdi.web.entity.vo;

import lombok.Data;

@Data
public class HouseVo {
    private Integer id;
    private Integer chicksDistributionId;
    private String name;
    private Integer capacity;
    private Integer chickCount;
    private Integer stock;
}
