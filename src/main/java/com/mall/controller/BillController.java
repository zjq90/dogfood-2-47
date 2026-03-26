package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Bill;
import com.mall.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {
    @Autowired
    private BillService billService;

    @GetMapping
    public String billPage() {
        return "bill/list";
    }

    @GetMapping("/list")
    @ResponseBody
    public Result<List<Bill>> getBillList(@RequestParam(required = false) String billNo,
                                          @RequestParam(required = false) String orderNo,
                                          @RequestParam(required = false) Integer billType,
                                          @RequestParam(required = false) Integer billStatus) {
        return billService.getBillList(billNo, orderNo, billType, billStatus);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Result<Bill> getBillById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    @PostMapping
    @ResponseBody
    public Result<String> addBill(@RequestBody Bill bill) {
        return billService.addBill(bill);
    }

    @PutMapping
    @ResponseBody
    public Result<String> updateBill(@RequestBody Bill bill) {
        return billService.updateBill(bill);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result<String> deleteBill(@PathVariable Long id) {
        return billService.deleteBill(id);
    }

    @GetMapping("/export")
    public void exportBill(HttpServletResponse response,
                           @RequestParam(required = false) String billNo,
                           @RequestParam(required = false) String orderNo,
                           @RequestParam(required = false) Integer billType,
                           @RequestParam(required = false) Integer billStatus) {
        billService.exportBill(response, billNo, orderNo, billType, billStatus);
    }
}