package com.mall.controller;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Bill;
import com.mall.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/bill")
public class BillController {
    
    @Autowired
    private BillService billService;
    
    @GetMapping("/list")
    public String listPage() {
        return "bill/list";
    }
    
    @GetMapping("/data")
    @ResponseBody
    public Result<PageResult<Bill>> getBillList(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return billService.getBillList(type, startTime, endTime, pageNum, pageSize);
    }
    
    @GetMapping("/add")
    public String addPage() {
        return "bill/add";
    }
    
    @PostMapping("/add")
    @ResponseBody
    public Result<Void> addBill(@RequestBody Bill bill) {
        return billService.addBill(bill);
    }
    
    @GetMapping("/edit/{id}")
    @ResponseBody
    public Result<Bill> getBillDetail(@PathVariable Long id) {
        return billService.getBillById(id);
    }
    
    @PostMapping("/update")
    @ResponseBody
    public Result<Void> updateBill(@RequestBody Bill bill) {
        return billService.updateBill(bill);
    }
    
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<Void> deleteBill(@PathVariable Long id) {
        return billService.deleteBill(id);
    }
    
    @GetMapping("/export")
    public void exportBills(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            HttpServletResponse response) throws IOException {
        billService.exportBills(type, startTime, endTime, response);
    }
}
