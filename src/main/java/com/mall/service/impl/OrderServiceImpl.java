package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Order;
import com.mall.mapper.OrderMapper;
import com.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "order:";
    private static final long REDIS_EXPIRE_TIME = 30;

    @Override
    public Result<Order> getOrderById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        Order order = (Order) redisTemplate.opsForValue().get(redisKey);
        if (order == null) {
            order = orderMapper.selectById(id);
            if (order != null) {
                redisTemplate.opsForValue().set(redisKey, order, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        return Result.success(order);
    }

    @Override
    public Result<Order> getOrderByOrderNo(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        return Result.success(order);
    }

    @Override
    public Result<List<Order>> getOrderList(String orderNo, String username, Integer orderStatus) {
        List<Order> orderList = orderMapper.selectList(orderNo, username, orderStatus);
        return Result.success(orderList);
    }

    @Override
    public Result<String> updateOrder(Order order) {
        int result = orderMapper.update(order);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + order.getId();
            redisTemplate.delete(redisKey);
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @Override
    public Result<String> closeOrder(Long id) {
        int result = orderMapper.updateOrderStatus(id, 5);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("订单关闭成功");
        }
        return Result.error("订单关闭失败");
    }

    @Override
    public Result<String> deleteOrder(Long id) {
        int result = orderMapper.deleteById(id);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}