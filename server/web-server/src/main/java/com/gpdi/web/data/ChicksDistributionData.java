package com.gpdi.web.data;

import lombok.Data;

@Data
public class ChicksDistributionData {
    private Integer farmId;
    private Integer houseId;
    private Integer chicksId;
    private Integer produceCycle;
    private String chickType;
}
