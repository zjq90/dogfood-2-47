package com.mall.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String billNo;
    private Integer type;
    private BigDecimal amount;
    private String title;
    private String description;
    private Long orderId;
    private Integer status;
    private LocalDateTime createTime;
    
    private String orderNo;
    
    public static final int TYPE_INCOME = 1;
    public static final int TYPE_EXPENSE = 2;
    
    public static final int STATUS_INVALID = 0;
    public static final int STATUS_VALID = 1;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBillNo() {
        return billNo;
    }
    
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public String getTypeText() {
        return type != null && type == 1 ? "收入" : "支出";
    }
    
    public String getStatusText() {
        return status != null && status == 1 ? "有效" : "无效";
    }
}
