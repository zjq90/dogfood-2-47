package com.mall.service.impl;

import com.mall.entity.Orders;
import com.mall.mapper.OrdersMapper;
import com.mall.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OrdersServiceImpl implements OrdersService {
    
    private static final String ORDER_CACHE_PREFIX = "order:";
    
    @Autowired
    private OrdersMapper ordersMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public Orders getById(Long id) {
        String key = ORDER_CACHE_PREFIX + id;
        Orders orders = (Orders) redisTemplate.opsForValue().get(key);
        if (orders == null) {
            orders = ordersMapper.selectById(id);
            if (orders != null) {
                redisTemplate.opsForValue().set(key, orders, 30, TimeUnit.MINUTES);
            }
        }
        return orders;
    }
    
    @Override
    public Orders getByOrderNo(String orderNo) {
        return ordersMapper.selectByOrderNo(orderNo);
    }
    
    @Override
    public List<Orders> getList(Orders orders) {
        return ordersMapper.selectList(orders);
    }
    
    @Override
    public boolean save(Orders orders) {
        return ordersMapper.insert(orders) > 0;
    }
    
    @Override
    public boolean update(Orders orders) {
        int result = ordersMapper.update(orders);
        if (result > 0) {
            redisTemplate.delete(ORDER_CACHE_PREFIX + orders.getId());
        }
        return result > 0;
    }
    
    @Override
    public boolean updateStatus(Long id, Integer status) {
        int result = ordersMapper.updateStatus(id, status);
        if (result > 0) {
            redisTemplate.delete(ORDER_CACHE_PREFIX + id);
        }
        return result > 0;
    }
    
    @Override
    public boolean closeOrder(Long id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders == null || orders.getStatus() == 4) {
            return false;
        }
        int result = ordersMapper.updateStatus(id, 4);
        if (result > 0) {
            redisTemplate.delete(ORDER_CACHE_PREFIX + id);
        }
        return result > 0;
    }
    
    @Override
    public boolean deleteById(Long id) {
        int result = ordersMapper.deleteById(id);
        if (result > 0) {
            redisTemplate.delete(ORDER_CACHE_PREFIX + id);
        }
        return result > 0;
    }
}
