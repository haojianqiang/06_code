package com.gpdi.web.data.process.feeding;

import lombok.Data;

@Data
public class FeedingData {
    private Integer farmId;
    private Integer houseId;
    private Integer raiseId;
    private Integer chicksCcsId;
    private String raiseTimeStart;
    private String raiseTimeEnd;
    private Double raiseValueStart;
    private Double raiseValueEnd;
}
