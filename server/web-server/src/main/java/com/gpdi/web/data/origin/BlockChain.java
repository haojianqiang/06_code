package com.gpdi.web.data.origin;

import lombok.Data;

import java.util.Date;

@Data
public class BlockChain {
    private String sourceCode;
    private String hash;
    private String city;
    private String town;
    private String farm;
    private String house;
    private Date createTime;
}
