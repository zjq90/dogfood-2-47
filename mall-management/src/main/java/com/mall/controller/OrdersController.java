package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Orders;
import com.mall.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    
    @Autowired
    private OrdersService ordersService;
    
    @GetMapping("/list")
    public Result<List<Orders>> list(Orders orders) {
        List<Orders> list = ordersService.getList(orders);
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<Orders> getById(@PathVariable Long id) {
        Orders orders = ordersService.getById(id);
        return Result.success(orders);
    }
    
    @PostMapping("/save")
    public Result<String> save(@RequestBody Orders orders) {
        boolean result = ordersService.save(orders);
        if (result) {
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }
    
    @PostMapping("/update")
    public Result<String> update(@RequestBody Orders orders) {
        boolean result = ordersService.update(orders);
        if (result) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
    
    @PostMapping("/close/{id}")
    public Result<String> closeOrder(@PathVariable Long id) {
        boolean result = ordersService.closeOrder(id);
        if (result) {
            return Result.success("订单已关闭");
        }
        return Result.error("关闭订单失败");
    }
    
    @PostMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean result = ordersService.deleteById(id);
        if (result) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
