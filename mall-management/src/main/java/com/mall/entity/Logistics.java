package com.mall.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Logistics implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long orderId;
    private String logisticsNo;
    private String logisticsCompany;
    private Integer status;
    private String currentLocation;
    private Date updateTime;
    private Date createTime;
    
    private String orderNo;
    private String receiverName;
    private List<LogisticsTrace> traces;
}
