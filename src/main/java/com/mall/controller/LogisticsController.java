package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Logistics;
import com.mall.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/logistics")
public class LogisticsController {
    @Autowired
    private LogisticsService logisticsService;

    @GetMapping
    public String logisticsPage() {
        return "logistics/list";
    }

    @GetMapping("/list")
    @ResponseBody
    public Result<List<Logistics>> getLogisticsList(@RequestParam(required = false) String orderNo,
                                                    @RequestParam(required = false) Integer shippingStatus) {
        return logisticsService.getLogisticsList(orderNo, shippingStatus);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Result<Logistics> getLogisticsById(@PathVariable Long id) {
        return logisticsService.getLogisticsById(id);
    }

    @GetMapping("/order/{orderId}")
    @ResponseBody
    public Result<Logistics> getLogisticsByOrderId(@PathVariable Long orderId) {
        return logisticsService.getLogisticsByOrderId(orderId);
    }

    @PostMapping
    @ResponseBody
    public Result<String> addLogistics(@RequestBody Logistics logistics) {
        return logisticsService.addLogistics(logistics);
    }

    @PutMapping
    @ResponseBody
    public Result<String> updateLogistics(@RequestBody Logistics logistics) {
        return logisticsService.updateLogistics(logistics);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result<String> deleteLogistics(@PathVariable Long id) {
        return logisticsService.deleteLogistics(id);
    }
}