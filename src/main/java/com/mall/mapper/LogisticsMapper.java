package com.mall.mapper;

import com.mall.entity.Logistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogisticsMapper {
    Logistics selectById(@Param("id") Long id);

    Logistics selectByOrderId(@Param("orderId") Long orderId);

    List<Logistics> selectList(@Param("orderNo") String orderNo, @Param("shippingStatus") Integer shippingStatus);

    int insert(Logistics logistics);

    int update(Logistics logistics);

    int deleteById(@Param("id") Long id);
}