package com.mall.service;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Logistics;

public interface LogisticsService {
    
    Result<PageResult<Logistics>> getLogisticsList(String orderNo, String logisticsNo, 
                                                   Integer pageNum, Integer pageSize);
    
    Result<Logistics> getLogisticsById(Long id);
    
    Result<Logistics> getLogisticsByOrderId(Long orderId);
    
    Result<Void> addLogistics(Logistics logistics);
    
    Result<Void> updateLogistics(Logistics logistics);
    
    Result<Void> updateLogisticsByOrderId(Long orderId, String status, String currentLocation, String details);
    
    Result<Void> deleteLogistics(Long id);
}
