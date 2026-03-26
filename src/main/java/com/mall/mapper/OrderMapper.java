package com.mall.mapper;

import com.mall.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    
    Order selectById(Long id);
    
    Order selectByOrderNo(String orderNo);
    
    List<Order> selectList(@Param("orderNo") String orderNo, @Param("status") Integer status, 
                           @Param("userId") Long userId);
    
    int insert(Order order);
    
    int update(Order order);
    
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    int updateLogistics(@Param("id") Long id, @Param("logisticsNo") String logisticsNo, 
                        @Param("logisticsCompany") String logisticsCompany);
    
    int closeOrder(@Param("id") Long id);
    
    long count();
    
    long countByStatus(Integer status);
}
