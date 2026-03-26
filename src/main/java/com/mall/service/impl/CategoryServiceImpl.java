package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Category;
import com.mall.mapper.CategoryMapper;
import com.mall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "category:";
    private static final String REDIS_KEY_ALL = "category:all";
    private static final long REDIS_EXPIRE_TIME = 60;

    @Override
    public Result<Category> getCategoryById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        Category category = (Category) redisTemplate.opsForValue().get(redisKey);
        if (category == null) {
            category = categoryMapper.selectById(id);
            if (category != null) {
                redisTemplate.opsForValue().set(redisKey, category, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        return Result.success(category);
    }

    @Override
    public Result<List<Category>> getAllCategories() {
        List<Category> categoryList = (List<Category>) redisTemplate.opsForValue().get(REDIS_KEY_ALL);
        if (categoryList == null) {
            categoryList = categoryMapper.selectAll();
            if (categoryList != null && !categoryList.isEmpty()) {
                redisTemplate.opsForValue().set(REDIS_KEY_ALL, categoryList, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        return Result.success(categoryList);
    }

    @Override
    public Result<List<Category>> getCategoryList(String name, Integer status) {
        List<Category> categoryList = categoryMapper.selectList(name, status);
        return Result.success(categoryList);
    }

    @Override
    public Result<String> addCategory(Category category) {
        int result = categoryMapper.insert(category);
        if (result > 0) {
            redisTemplate.delete(REDIS_KEY_ALL);
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result<String> updateCategory(Category category) {
        int result = categoryMapper.update(category);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + category.getId();
            redisTemplate.delete(redisKey);
            redisTemplate.delete(REDIS_KEY_ALL);
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @Override
    public Result<String> updateCategoryStatus(Long id, Integer status) {
        int result = categoryMapper.updateStatus(id, status);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            redisTemplate.delete(REDIS_KEY_ALL);
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }

    @Override
    public Result<String> deleteCategory(Long id) {
        int result = categoryMapper.deleteById(id);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            redisTemplate.delete(REDIS_KEY_ALL);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}