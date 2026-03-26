package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Logistics;
import com.mall.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {
    
    @Autowired
    private LogisticsService logisticsService;
    
    @GetMapping("/list")
    public Result<List<Logistics>> list(Logistics logistics) {
        List<Logistics> list = logisticsService.getList(logistics);
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<Logistics> getById(@PathVariable Long id) {
        Logistics logistics = logisticsService.getById(id);
        return Result.success(logistics);
    }
    
    @GetMapping("/order/{orderId}")
    public Result<Logistics> getByOrderId(@PathVariable Long orderId) {
        Logistics logistics = logisticsService.getByOrderId(orderId);
        return Result.success(logistics);
    }
    
    @PostMapping("/save")
    public Result<String> save(@RequestBody Logistics logistics) {
        boolean result = logisticsService.save(logistics);
        if (result) {
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }
    
    @PostMapping("/update")
    public Result<String> update(@RequestBody Logistics logistics) {
        boolean result = logisticsService.update(logistics);
        if (result) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
    
    @PostMapping("/updateInfo")
    public Result<String> updateLogisticsInfo(@RequestParam Long id,
                                              @RequestParam(required = false) String logisticsNo,
                                              @RequestParam(required = false) String logisticsCompany,
                                              @RequestParam(required = false) Integer status,
                                              @RequestParam(required = false) String currentLocation) {
        boolean result = logisticsService.updateLogisticsInfo(id, logisticsNo, logisticsCompany, status, currentLocation);
        if (result) {
            return Result.success("物流信息更新成功");
        }
        return Result.error("物流信息更新失败");
    }
    
    @PostMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean result = logisticsService.deleteById(id);
        if (result) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
