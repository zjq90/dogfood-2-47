package com.mall.service;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Order;

public interface OrderService {
    
    Result<PageResult<Order>> getOrderList(String orderNo, Integer status, Long userId, 
                                           Integer pageNum, Integer pageSize);
    
    Result<Order> getOrderById(Long id);
    
    Result<Order> getOrderByOrderNo(String orderNo);
    
    Result<Void> closeOrder(Long id);
    
    Result<Void> shipOrder(Long id, String logisticsNo, String logisticsCompany);
    
    Result<Void> updateOrderStatus(Long id, Integer status);
}
