package com.mall.mapper;

import com.mall.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    Order selectById(@Param("id") Long id);

    Order selectByOrderNo(@Param("orderNo") String orderNo);

    List<Order> selectList(@Param("orderNo") String orderNo, @Param("username") String username, @Param("orderStatus") Integer orderStatus);

    int insert(Order order);

    int update(Order order);

    int updateOrderStatus(@Param("id") Long id, @Param("orderStatus") Integer orderStatus);

    int deleteById(@Param("id") Long id);
}