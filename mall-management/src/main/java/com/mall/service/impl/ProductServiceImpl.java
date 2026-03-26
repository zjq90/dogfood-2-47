package com.mall.service.impl;

import com.mall.entity.Product;
import com.mall.mapper.ProductMapper;
import com.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {
    
    private static final String PRODUCT_CACHE_PREFIX = "product:";
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public Product getById(Long id) {
        String key = PRODUCT_CACHE_PREFIX + id;
        Product product = (Product) redisTemplate.opsForValue().get(key);
        if (product == null) {
            product = productMapper.selectById(id);
            if (product != null) {
                redisTemplate.opsForValue().set(key, product, 30, TimeUnit.MINUTES);
            }
        }
        return product;
    }
    
    @Override
    public List<Product> getList(Product product) {
        return productMapper.selectList(product);
    }
    
    @Override
    public boolean save(Product product) {
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        return productMapper.insert(product) > 0;
    }
    
    @Override
    public boolean update(Product product) {
        int result = productMapper.update(product);
        if (result > 0) {
            redisTemplate.delete(PRODUCT_CACHE_PREFIX + product.getId());
        }
        return result > 0;
    }
    
    @Override
    public boolean updateStock(Long id, Integer stock) {
        int result = productMapper.updateStock(id, stock);
        if (result > 0) {
            redisTemplate.delete(PRODUCT_CACHE_PREFIX + id);
        }
        return result > 0;
    }
    
    @Override
    public boolean updateStatus(Long id, Integer status) {
        int result = productMapper.updateStatus(id, status);
        if (result > 0) {
            redisTemplate.delete(PRODUCT_CACHE_PREFIX + id);
        }
        return result > 0;
    }
    
    @Override
    public boolean deleteById(Long id) {
        int result = productMapper.deleteById(id);
        if (result > 0) {
            redisTemplate.delete(PRODUCT_CACHE_PREFIX + id);
        }
        return result > 0;
    }
}
