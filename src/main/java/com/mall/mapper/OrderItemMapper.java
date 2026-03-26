package com.mall.mapper;

import com.mall.entity.OrderItem;

import java.util.List;

public interface OrderItemMapper {
    
    List<OrderItem> selectByOrderId(Long orderId);
    
    int insert(OrderItem item);
    
    int insertBatch(List<OrderItem> items);
    
    int deleteByOrderId(Long orderId);
}
