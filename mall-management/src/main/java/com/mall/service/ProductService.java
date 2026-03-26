package com.mall.service;

import com.mall.entity.Product;
import java.util.List;

public interface ProductService {
    
    Product getById(Long id);
    
    List<Product> getList(Product product);
    
    boolean save(Product product);
    
    boolean update(Product product);
    
    boolean updateStock(Long id, Integer stock);
    
    boolean updateStatus(Long id, Integer status);
    
    boolean deleteById(Long id);
}
