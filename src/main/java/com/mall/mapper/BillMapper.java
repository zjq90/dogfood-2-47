package com.mall.mapper;

import com.mall.entity.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillMapper {
    
    Bill selectById(Long id);
    
    List<Bill> selectList(@Param("type") Integer type, @Param("startTime") String startTime, 
                          @Param("endTime") String endTime);
    
    int insert(Bill bill);
    
    int update(Bill bill);
    
    int deleteById(Long id);
    
    long count();
}
