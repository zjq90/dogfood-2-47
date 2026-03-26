package com.mall.mapper;

import com.mall.entity.Category;

import java.util.List;

public interface CategoryMapper {
    
    Category selectById(Long id);
    
    List<Category> selectAll();
    
    List<Category> selectByParentId(Long parentId);
    
    int insert(Category category);
    
    int update(Category category);
    
    int updateStatus(Long id, Integer status);
    
    int deleteById(Long id);
}
