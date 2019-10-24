package com.gpdi.web.data.process.prevention;

import lombok.Data;

@Data
public class PreventionData {
    private Integer farmId;
    private Integer houseId;
    private Integer medicineId;
    private Integer vaccineId;
    private String raiseTimeStart;
    private String raiseTimeEnd;
}
