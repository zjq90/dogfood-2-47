package com.mall.service;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductService {
    
    Result<PageResult<Product>> getProductList(String name, Long categoryId, Integer status, 
                                                Integer pageNum, Integer pageSize);
    
    Result<Product> getProductById(Long id);
    
    Result<Void> addProduct(Product product);
    
    Result<Void> updateProduct(Product product);
    
    Result<Void> updateProductStatus(Long id, Integer status);
    
    Result<Void> deleteProduct(Long id);
    
    Result<String> uploadImage(MultipartFile file, HttpServletRequest request);
}
