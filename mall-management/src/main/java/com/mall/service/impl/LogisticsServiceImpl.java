package com.mall.service.impl;

import com.mall.entity.Logistics;
import com.mall.entity.LogisticsTrace;
import com.mall.mapper.LogisticsMapper;
import com.mall.mapper.LogisticsTraceMapper;
import com.mall.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LogisticsServiceImpl implements LogisticsService {
    
    @Autowired
    private LogisticsMapper logisticsMapper;
    
    @Autowired
    private LogisticsTraceMapper logisticsTraceMapper;
    
    @Override
    public Logistics getById(Long id) {
        Logistics logistics = logisticsMapper.selectById(id);
        if (logistics != null) {
            List<LogisticsTrace> traces = logisticsTraceMapper.selectByLogisticsId(id);
            logistics.setTraces(traces);
        }
        return logistics;
    }
    
    @Override
    public Logistics getByOrderId(Long orderId) {
        Logistics logistics = logisticsMapper.selectByOrderId(orderId);
        if (logistics != null) {
            List<LogisticsTrace> traces = logisticsTraceMapper.selectByLogisticsId(logistics.getId());
            logistics.setTraces(traces);
        }
        return logistics;
    }
    
    @Override
    public List<Logistics> getList(Logistics logistics) {
        return logisticsMapper.selectList(logistics);
    }
    
    @Override
    public boolean save(Logistics logistics) {
        return logisticsMapper.insert(logistics) > 0;
    }
    
    @Override
    public boolean update(Logistics logistics) {
        return logisticsMapper.update(logistics) > 0;
    }
    
    @Override
    public boolean deleteById(Long id) {
        return logisticsMapper.deleteById(id) > 0;
    }
    
    @Override
    @Transactional
    public boolean updateLogisticsInfo(Long id, String logisticsNo, String logisticsCompany, Integer status, String currentLocation) {
        Logistics logistics = logisticsMapper.selectById(id);
        if (logistics == null) {
            return false;
        }
        
        logistics.setLogisticsNo(logisticsNo);
        logistics.setLogisticsCompany(logisticsCompany);
        logistics.setStatus(status);
        logistics.setCurrentLocation(currentLocation);
        
        int result = logisticsMapper.update(logistics);
        
        if (result > 0 && currentLocation != null) {
            LogisticsTrace trace = new LogisticsTrace();
            trace.setLogisticsId(id);
            trace.setLocation(currentLocation);
            String description = getStatusDescription(status);
            trace.setDescription(description);
            logisticsTraceMapper.insert(trace);
        }
        
        return result > 0;
    }
    
    private String getStatusDescription(Integer status) {
        switch (status) {
            case 0: return "待发货";
            case 1: return "已发货";
            case 2: return "运输中";
            case 3: return "派送中";
            case 4: return "已签收";
            default: return "未知状态";
        }
    }
}
