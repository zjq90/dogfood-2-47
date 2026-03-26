package com.mall.mapper;

import com.mall.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductMapper {
    
    Product selectById(Long id);
    
    List<Product> selectList(Product product);
    
    int insert(Product product);
    
    int update(Product product);
    
    int updateStock(@Param("id") Long id, @Param("stock") Integer stock);
    
    int deleteById(Long id);
    
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
