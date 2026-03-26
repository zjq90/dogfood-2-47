package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Category;
import com.mall.mapper.CategoryMapper;
import com.mall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String CATEGORY_CACHE_KEY = "category:all";
    
    @Override
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = (List<Category>) redisTemplate.opsForValue().get(CATEGORY_CACHE_KEY);
        
        if (categories == null) {
            categories = categoryMapper.selectAll();
            redisTemplate.opsForValue().set(CATEGORY_CACHE_KEY, categories, 30, TimeUnit.MINUTES);
        }
        
        return Result.success(categories);
    }
    
    @Override
    public Result<List<Category>> getCategoryTree() {
        List<Category> allCategories = categoryMapper.selectAll();
        
        List<Category> rootCategories = allCategories.stream()
                .filter(c -> c.getParentId() == null || c.getParentId() == 0)
                .collect(Collectors.toList());
        
        for (Category root : rootCategories) {
            buildCategoryTree(root, allCategories);
        }
        
        return Result.success(rootCategories);
    }
    
    private void buildCategoryTree(Category parent, List<Category> allCategories) {
        List<Category> children = allCategories.stream()
                .filter(c -> parent.getId().equals(c.getParentId()))
                .collect(Collectors.toList());
        
        if (!children.isEmpty()) {
            parent.setChildren(children);
            for (Category child : children) {
                buildCategoryTree(child, allCategories);
            }
        }
    }
    
    @Override
    public Result<Category> getCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        return Result.success(category);
    }
    
    @Override
    public Result<Void> addCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return Result.error("分类名称不能为空");
        }
        
        if (category.getParentId() == null) {
            category.setParentId(0L);
            category.setLevel(1);
        } else {
            Category parent = categoryMapper.selectById(category.getParentId());
            if (parent != null) {
                category.setLevel(parent.getLevel() + 1);
            } else {
                category.setLevel(1);
            }
        }
        
        if (category.getSort() == null) {
            category.setSort(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(Category.STATUS_ENABLED);
        }
        
        categoryMapper.insert(category);
        clearCategoryCache();
        
        return Result.success("添加成功", null);
    }
    
    @Override
    public Result<Void> updateCategory(Category category) {
        if (category.getId() == null) {
            return Result.error("分类ID不能为空");
        }
        
        Category existCategory = categoryMapper.selectById(category.getId());
        if (existCategory == null) {
            return Result.error("分类不存在");
        }
        
        categoryMapper.update(category);
        clearCategoryCache();
        
        return Result.success("更新成功", null);
    }
    
    @Override
    public Result<Void> updateCategoryStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return Result.error("参数错误");
        }
        
        categoryMapper.updateStatus(id, status);
        clearCategoryCache();
        
        return Result.success("操作成功", null);
    }
    
    @Override
    public Result<Void> deleteCategory(Long id) {
        List<Category> children = categoryMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            return Result.error("该分类下存在子分类，无法删除");
        }
        
        categoryMapper.deleteById(id);
        clearCategoryCache();
        
        return Result.success("删除成功", null);
    }
    
    private void clearCategoryCache() {
        redisTemplate.delete(CATEGORY_CACHE_KEY);
    }
}
