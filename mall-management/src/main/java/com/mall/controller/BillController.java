package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Bill;
import com.mall.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/bill")
public class BillController {
    
    @Autowired
    private BillService billService;
    
    @GetMapping("/list")
    public Result<List<Bill>> list(Bill bill) {
        List<Bill> list = billService.getList(bill);
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<Bill> getById(@PathVariable Long id) {
        Bill bill = billService.getById(id);
        return Result.success(bill);
    }
    
    @PostMapping("/save")
    public Result<String> save(@RequestBody Bill bill) {
        boolean result = billService.save(bill);
        if (result) {
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }
    
    @PostMapping("/update")
    public Result<String> update(@RequestBody Bill bill) {
        boolean result = billService.update(bill);
        if (result) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
    
    @PostMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean result = billService.deleteById(id);
        if (result) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @GetMapping("/export")
    public void export(@RequestParam(required = false) String startTime,
                       @RequestParam(required = false) String endTime,
                       HttpServletResponse response) {
        billService.exportBills(startTime, endTime, response);
    }
}
