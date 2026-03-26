package com.mall.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class LogisticsTrace implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long logisticsId;
    private String location;
    private String description;
    private Date createTime;
}
