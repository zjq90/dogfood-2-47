package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Category;
import java.util.List;

public interface CategoryService {
    Result<Category> getCategoryById(Long id);

    Result<List<Category>> getAllCategories();

    Result<List<Category>> getCategoryList(String name, Integer status);

    Result<String> addCategory(Category category);

    Result<String> updateCategory(Category category);

    Result<String> updateCategoryStatus(Long id, Integer status);

    Result<String> deleteCategory(Long id);
}