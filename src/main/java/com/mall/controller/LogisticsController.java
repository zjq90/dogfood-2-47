package com.mall.controller;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Logistics;
import com.mall.entity.Order;
import com.mall.service.LogisticsService;
import com.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/logistics")
public class LogisticsController {
    
    @Autowired
    private LogisticsService logisticsService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/list")
    public String listPage() {
        return "logistics/list";
    }
    
    @GetMapping("/data")
    @ResponseBody
    public Result<PageResult<Logistics>> getLogisticsList(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String logisticsNo,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return logisticsService.getLogisticsList(orderNo, logisticsNo, pageNum, pageSize);
    }
    
    @GetMapping("/add/{orderId}")
    public String addPage(@PathVariable Long orderId, Model model) {
        Result<Order> orderResult = orderService.getOrderById(orderId);
        if (orderResult.isSuccess()) {
            model.addAttribute("order", orderResult.getData());
        }
        return "logistics/add";
    }
    
    @PostMapping("/add")
    @ResponseBody
    public Result<Void> addLogistics(@RequestBody Logistics logistics) {
        return logisticsService.addLogistics(logistics);
    }
    
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Result<Logistics> getLogisticsDetail(@PathVariable Long id) {
        return logisticsService.getLogisticsById(id);
    }
    
    @GetMapping("/byOrder/{orderId}")
    @ResponseBody
    public Result<Logistics> getLogisticsByOrderId(@PathVariable Long orderId) {
        return logisticsService.getLogisticsByOrderId(orderId);
    }
    
    @PostMapping("/update")
    @ResponseBody
    public Result<Void> updateLogistics(@RequestBody Logistics logistics) {
        return logisticsService.updateLogistics(logistics);
    }
    
    @PostMapping("/updateByOrder")
    @ResponseBody
    public Result<Void> updateLogisticsByOrderId(@RequestParam Long orderId,
                                                  @RequestParam String status,
                                                  @RequestParam String currentLocation,
                                                  @RequestParam String details) {
        return logisticsService.updateLogisticsByOrderId(orderId, status, currentLocation, details);
    }
    
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<Void> deleteLogistics(@PathVariable Long id) {
        return logisticsService.deleteLogistics(id);
    }
}
