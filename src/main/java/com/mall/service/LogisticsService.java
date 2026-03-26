package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Logistics;
import java.util.List;

public interface LogisticsService {
    Result<Logistics> getLogisticsById(Long id);

    Result<Logistics> getLogisticsByOrderId(Long orderId);

    Result<List<Logistics>> getLogisticsList(String orderNo, Integer shippingStatus);

    Result<String> addLogistics(Logistics logistics);

    Result<String> updateLogistics(Logistics logistics);

    Result<String> deleteLogistics(Long id);
}