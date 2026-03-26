package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Product;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ProductService {
    Result<Product> getProductById(Long id);

    Result<List<Product>> getProductList(String name, Long categoryId, Integer status);

    Result<String> addProduct(Product product);

    Result<String> updateProduct(Product product);

    Result<String> updateProductStatus(Long id, Integer status);

    Result<String> deleteProduct(Long id);

    Result<String> uploadImage(MultipartFile file);
}