package com.mall.mapper;

import com.mall.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrdersMapper {
    
    Orders selectById(Long id);
    
    Orders selectByOrderNo(String orderNo);
    
    List<Orders> selectList(Orders orders);
    
    int insert(Orders orders);
    
    int update(Orders orders);
    
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    int deleteById(Long id);
}
