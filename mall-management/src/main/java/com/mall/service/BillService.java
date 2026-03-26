package com.mall.service;

import com.mall.entity.Bill;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BillService {
    
    Bill getById(Long id);
    
    List<Bill> getList(Bill bill);
    
    boolean save(Bill bill);
    
    boolean update(Bill bill);
    
    boolean deleteById(Long id);
    
    void exportBills(String startTime, String endTime, HttpServletResponse response);
}
