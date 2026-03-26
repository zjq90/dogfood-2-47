package com.mall.service;

import com.mall.entity.Logistics;
import java.util.List;

public interface LogisticsService {
    
    Logistics getById(Long id);
    
    Logistics getByOrderId(Long orderId);
    
    List<Logistics> getList(Logistics logistics);
    
    boolean save(Logistics logistics);
    
    boolean update(Logistics logistics);
    
    boolean deleteById(Long id);
    
    boolean updateLogisticsInfo(Long id, String logisticsNo, String logisticsCompany, Integer status, String currentLocation);
}
