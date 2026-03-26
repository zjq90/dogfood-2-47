package com.mall.mapper;

import com.mall.entity.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BillMapper {
    
    Bill selectById(Long id);
    
    List<Bill> selectList(Bill bill);
    
    int insert(Bill bill);
    
    int update(Bill bill);
    
    int deleteById(Long id);
    
    List<Bill> selectExportList(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
