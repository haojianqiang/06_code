package com.gpdi.web.entity.vo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class FarmTree {
    private Integer id;
    private String name;
    private Boolean disabled;
    private String uniqueId;
    private List<FarmTree> children;

}
