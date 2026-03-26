package com.mall.mapper;

import com.mall.entity.Logistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LogisticsMapper {
    
    Logistics selectById(Long id);
    
    Logistics selectByOrderId(Long orderId);
    
    List<Logistics> selectList(Logistics logistics);
    
    int insert(Logistics logistics);
    
    int update(Logistics logistics);
    
    int deleteById(Long id);
}
