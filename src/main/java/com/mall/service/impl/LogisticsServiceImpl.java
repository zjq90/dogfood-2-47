package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Logistics;
import com.mall.mapper.LogisticsMapper;
import com.mall.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LogisticsServiceImpl implements LogisticsService {
    @Autowired
    private LogisticsMapper logisticsMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "logistics:";
    private static final long REDIS_EXPIRE_TIME = 30;

    @Override
    public Result<Logistics> getLogisticsById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        Logistics logistics = (Logistics) redisTemplate.opsForValue().get(redisKey);
        if (logistics == null) {
            logistics = logisticsMapper.selectById(id);
            if (logistics != null) {
                redisTemplate.opsForValue().set(redisKey, logistics, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        return Result.success(logistics);
    }

    @Override
    public Result<Logistics> getLogisticsByOrderId(Long orderId) {
        Logistics logistics = logisticsMapper.selectByOrderId(orderId);
        return Result.success(logistics);
    }

    @Override
    public Result<List<Logistics>> getLogisticsList(String orderNo, Integer shippingStatus) {
        List<Logistics> logisticsList = logisticsMapper.selectList(orderNo, shippingStatus);
        return Result.success(logisticsList);
    }

    @Override
    public Result<String> addLogistics(Logistics logistics) {
        int result = logisticsMapper.insert(logistics);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result<String> updateLogistics(Logistics logistics) {
        int result = logisticsMapper.update(logistics);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + logistics.getId();
            redisTemplate.delete(redisKey);
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @Override
    public Result<String> deleteLogistics(Long id) {
        int result = logisticsMapper.deleteById(id);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}