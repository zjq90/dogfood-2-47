package com.mall.service;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Bill;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface BillService {
    
    Result<PageResult<Bill>> getBillList(Integer type, String startTime, String endTime, 
                                         Integer pageNum, Integer pageSize);
    
    Result<Bill> getBillById(Long id);
    
    Result<Void> addBill(Bill bill);
    
    Result<Void> updateBill(Bill bill);
    
    Result<Void> deleteBill(Long id);
    
    void exportBills(Integer type, String startTime, String endTime, HttpServletResponse response) throws IOException;
}
