package com.mall.service;

import com.mall.entity.Orders;
import java.util.List;

public interface OrdersService {
    
    Orders getById(Long id);
    
    Orders getByOrderNo(String orderNo);
    
    List<Orders> getList(Orders orders);
    
    boolean save(Orders orders);
    
    boolean update(Orders orders);
    
    boolean updateStatus(Long id, Integer status);
    
    boolean closeOrder(Long id);
    
    boolean deleteById(Long id);
}
