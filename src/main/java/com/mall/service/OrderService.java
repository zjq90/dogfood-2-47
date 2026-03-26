package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Order;
import java.util.List;

public interface OrderService {
    Result<Order> getOrderById(Long id);

    Result<Order> getOrderByOrderNo(String orderNo);

    Result<List<Order>> getOrderList(String orderNo, String username, Integer orderStatus);

    Result<String> updateOrder(Order order);

    Result<String> closeOrder(Long id);

    Result<String> deleteOrder(Long id);
}