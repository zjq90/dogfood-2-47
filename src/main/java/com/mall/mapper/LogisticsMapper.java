package com.mall.mapper;

import com.mall.entity.Logistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogisticsMapper {
    
    Logistics selectById(Long id);
    
    Logistics selectByOrderId(Long orderId);
    
    List<Logistics> selectList(@Param("orderNo") String orderNo, @Param("logisticsNo") String logisticsNo);
    
    int insert(Logistics logistics);
    
    int update(Logistics logistics);
    
    int updateByOrderId(Logistics logistics);
    
    int deleteById(Long id);
}
