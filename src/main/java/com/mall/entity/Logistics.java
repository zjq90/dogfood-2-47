package com.mall.entity;

import java.util.Date;

public class Logistics {
    private Long id;
    private Long orderId;
    private String orderNo;
    private String shippingCompany;
    private String shippingNo;
    private Integer shippingStatus;
    private String trackingInfo;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public Integer getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(Integer shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getTrackingInfo() {
        return trackingInfo;
    }

    public void setTrackingInfo(String trackingInfo) {
        this.trackingInfo = trackingInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}