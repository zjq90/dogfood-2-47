package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Logistics;
import com.mall.entity.Order;
import com.mall.mapper.LogisticsMapper;
import com.mall.mapper.OrderMapper;
import com.mall.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogisticsServiceImpl implements LogisticsService {
    
    @Autowired
    private LogisticsMapper logisticsMapper;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Override
    public Result<PageResult<Logistics>> getLogisticsList(String orderNo, String logisticsNo, 
                                                          Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        
        PageHelper.startPage(pageNum, pageSize);
        List<Logistics> list = logisticsMapper.selectList(orderNo, logisticsNo);
        PageInfo<Logistics> pageInfo = new PageInfo<>(list);
        
        PageResult<Logistics> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo.getList(), 
                                                            pageNum, pageSize);
        return Result.success(pageResult);
    }
    
    @Override
    public Result<Logistics> getLogisticsById(Long id) {
        Logistics logistics = logisticsMapper.selectById(id);
        if (logistics == null) {
            return Result.error("物流记录不存在");
        }
        return Result.success(logistics);
    }
    
    @Override
    public Result<Logistics> getLogisticsByOrderId(Long orderId) {
        Logistics logistics = logisticsMapper.selectByOrderId(orderId);
        if (logistics == null) {
            return Result.error("该订单暂无物流信息");
        }
        return Result.success(logistics);
    }
    
    @Override
    @Transactional
    public Result<Void> addLogistics(Logistics logistics) {
        if (logistics.getOrderId() == null) {
            return Result.error("订单ID不能为空");
        }
        if (logistics.getLogisticsNo() == null || logistics.getLogisticsNo().trim().isEmpty()) {
            return Result.error("物流单号不能为空");
        }
        
        Order order = orderMapper.selectById(logistics.getOrderId());
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        logistics.setUpdateTime(LocalDateTime.now());
        logisticsMapper.insert(logistics);
        
        orderMapper.updateLogistics(order.getId(), logistics.getLogisticsNo(), logistics.getCompany());
        
        return Result.success("添加成功", null);
    }
    
    @Override
    public Result<Void> updateLogistics(Logistics logistics) {
        if (logistics.getId() == null) {
            return Result.error("物流ID不能为空");
        }
        
        Logistics existLogistics = logisticsMapper.selectById(logistics.getId());
        if (existLogistics == null) {
            return Result.error("物流记录不存在");
        }
        
        logistics.setUpdateTime(LocalDateTime.now());
        logisticsMapper.update(logistics);
        
        return Result.success("更新成功", null);
    }
    
    @Override
    @Transactional
    public Result<Void> updateLogisticsByOrderId(Long orderId, String status, String currentLocation, String details) {
        if (orderId == null) {
            return Result.error("订单ID不能为空");
        }
        
        Logistics logistics = logisticsMapper.selectByOrderId(orderId);
        if (logistics == null) {
            return Result.error("该订单暂无物流信息");
        }
        
        Logistics updateLogistics = new Logistics();
        updateLogistics.setOrderId(orderId);
        updateLogistics.setStatus(status);
        updateLogistics.setCurrentLocation(currentLocation);
        updateLogistics.setDetails(details);
        updateLogistics.setUpdateTime(LocalDateTime.now());
        
        logisticsMapper.updateByOrderId(updateLogistics);
        
        return Result.success("更新成功", null);
    }
    
    @Override
    public Result<Void> deleteLogistics(Long id) {
        logisticsMapper.deleteById(id);
        return Result.success("删除成功", null);
    }
}
