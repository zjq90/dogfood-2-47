package com.mall.mapper;

import com.mall.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface OrderItemMapper {
    
    OrderItem selectById(Long id);
    
    List<OrderItem> selectByOrderId(Long orderId);
    
    int insert(OrderItem orderItem);
    
    int batchInsert(List<OrderItem> orderItems);
    
    int deleteByOrderId(Long orderId);
}
