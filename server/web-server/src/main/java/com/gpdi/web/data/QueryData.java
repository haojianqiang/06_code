package com.gpdi.web.data;

import lombok.Data;


@Data
public class QueryData {
    private String  name;
    private String  charger;
    private String  phoneNumber;
    private String  address;
    private String  farmName;
    private String  houseName;
    private Integer capacity;
    private Integer batchNumber;
    private String  chickType;
    private String  producer;
    private Integer houseId;
    private Integer farmId;
}
