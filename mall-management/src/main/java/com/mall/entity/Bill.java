package com.mall.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String billNo;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private Integer type;
    private String paymentMethod;
    private Integer status;
    private String remark;
    private Date createTime;
    
    private String userName;
    private String orderNo;
}
