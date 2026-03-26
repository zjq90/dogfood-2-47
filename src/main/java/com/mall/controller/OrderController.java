package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Order;
import com.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String orderPage() {
        return "order/list";
    }

    @GetMapping("/list")
    @ResponseBody
    public Result<List<Order>> getOrderList(@RequestParam(required = false) String orderNo,
                                            @RequestParam(required = false) String username,
                                            @RequestParam(required = false) Integer orderStatus) {
        return orderService.getOrderList(orderNo, username, orderStatus);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Result<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping
    @ResponseBody
    public Result<String> updateOrder(@RequestBody Order order) {
        return orderService.updateOrder(order);
    }

    @PutMapping("/{id}/close")
    @ResponseBody
    public Result<String> closeOrder(@PathVariable Long id) {
        return orderService.closeOrder(id);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result<String> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}