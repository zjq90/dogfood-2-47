package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Category;

import java.util.List;

public interface CategoryService {
    
    Result<List<Category>> getAllCategories();
    
    Result<List<Category>> getCategoryTree();
    
    Result<Category> getCategoryById(Long id);
    
    Result<Void> addCategory(Category category);
    
    Result<Void> updateCategory(Category category);
    
    Result<Void> updateCategoryStatus(Long id, Integer status);
    
    Result<Void> deleteCategory(Long id);
}
