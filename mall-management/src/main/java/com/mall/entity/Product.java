package com.mall.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private String description;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
