package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Order;
import com.mall.entity.OrderItem;
import com.mall.mapper.OrderItemMapper;
import com.mall.mapper.OrderMapper;
import com.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String ORDER_CACHE_KEY = "order:cache:";
    
    @Override
    public Result<PageResult<Order>> getOrderList(String orderNo, Integer status, Long userId, 
                                                  Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        
        PageHelper.startPage(pageNum, pageSize);
        List<Order> list = orderMapper.selectList(orderNo, status, userId);
        
        for (Order order : list) {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
            order.setItems(items);
        }
        
        PageInfo<Order> pageInfo = new PageInfo<>(list);
        PageResult<Order> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo.getList(), 
                                                        pageNum, pageSize);
        return Result.success(pageResult);
    }
    
    @Override
    public Result<Order> getOrderById(Long id) {
        String cacheKey = ORDER_CACHE_KEY + id;
        Order order = (Order) redisTemplate.opsForValue().get(cacheKey);
        
        if (order == null) {
            order = orderMapper.selectById(id);
            if (order != null) {
                List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
                order.setItems(items);
                redisTemplate.opsForValue().set(cacheKey, order, 30, TimeUnit.MINUTES);
            }
        }
        
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }
    
    @Override
    public Result<Order> getOrderByOrderNo(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
            order.setItems(items);
        }
        
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }
    
    @Override
    @Transactional
    public Result<Void> closeOrder(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (order.getStatus() == Order.STATUS_CLOSED) {
            return Result.error("订单已关闭");
        }
        
        if (order.getStatus() == Order.STATUS_COMPLETED) {
            return Result.error("订单已完成，无法关闭");
        }
        
        orderMapper.closeOrder(id);
        
        redisTemplate.delete(ORDER_CACHE_KEY + id);
        
        return Result.success("订单关闭成功", null);
    }
    
    @Override
    @Transactional
    public Result<Void> shipOrder(Long id, String logisticsNo, String logisticsCompany) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (order.getStatus() != Order.STATUS_PAID) {
            return Result.error("订单状态不正确，只有已付款订单可以发货");
        }
        
        orderMapper.updateLogistics(id, logisticsNo, logisticsCompany);
        orderMapper.updateStatus(id, Order.STATUS_SHIPPED);
        
        redisTemplate.delete(ORDER_CACHE_KEY + id);
        
        return Result.success("发货成功", null);
    }
    
    @Override
    public Result<Void> updateOrderStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return Result.error("参数错误");
        }
        
        orderMapper.updateStatus(id, status);
        
        redisTemplate.delete(ORDER_CACHE_KEY + id);
        
        return Result.success("操作成功", null);
    }
}
