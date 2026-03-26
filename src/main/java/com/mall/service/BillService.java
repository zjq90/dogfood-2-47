package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Bill;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BillService {
    Result<Bill> getBillById(Long id);

    Result<Bill> getBillByBillNo(String billNo);

    Result<List<Bill>> getBillList(String billNo, String orderNo, Integer billType, Integer billStatus);

    Result<String> addBill(Bill bill);

    Result<String> updateBill(Bill bill);

    Result<String> deleteBill(Long id);

    void exportBill(HttpServletResponse response, String billNo, String orderNo, Integer billType, Integer billStatus);
}