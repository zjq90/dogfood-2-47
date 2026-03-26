package com.mall.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Date createTime;
    private Date updateTime;
    
    private String userName;
    private List<OrderItem> orderItems;
}
