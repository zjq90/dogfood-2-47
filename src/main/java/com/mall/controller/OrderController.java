package com.mall.controller;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Order;
import com.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/list")
    public String listPage() {
        return "order/list";
    }
    
    @GetMapping("/data")
    @ResponseBody
    public Result<PageResult<Order>> getOrderList(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return orderService.getOrderList(orderNo, status, null, pageNum, pageSize);
    }
    
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Result<Order> getOrderDetail(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
    
    @GetMapping("/view/{id}")
    public String viewPage(@PathVariable Long id, Model model) {
        Result<Order> result = orderService.getOrderById(id);
        if (result.isSuccess()) {
            model.addAttribute("order", result.getData());
        }
        return "order/view";
    }
    
    @PostMapping("/close/{id}")
    @ResponseBody
    public Result<Void> closeOrder(@PathVariable Long id) {
        return orderService.closeOrder(id);
    }
    
    @PostMapping("/ship")
    @ResponseBody
    public Result<Void> shipOrder(@RequestParam Long id, 
                                   @RequestParam String logisticsNo,
                                   @RequestParam String logisticsCompany) {
        return orderService.shipOrder(id, logisticsNo, logisticsCompany);
    }
    
    @PostMapping("/updateStatus")
    @ResponseBody
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        return orderService.updateOrderStatus(id, status);
    }
}
