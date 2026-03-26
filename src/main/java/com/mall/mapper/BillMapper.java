package com.mall.mapper;

import com.mall.entity.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BillMapper {
    Bill selectById(@Param("id") Long id);

    Bill selectByBillNo(@Param("billNo") String billNo);

    List<Bill> selectList(@Param("billNo") String billNo, @Param("orderNo") String orderNo, 
                          @Param("billType") Integer billType, @Param("billStatus") Integer billStatus);

    List<Bill> selectAll();

    int insert(Bill bill);

    int update(Bill bill);

    int deleteById(@Param("id") Long id);
}