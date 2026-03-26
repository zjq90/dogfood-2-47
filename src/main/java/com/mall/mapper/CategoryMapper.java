package com.mall.mapper;

import com.mall.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Category selectById(@Param("id") Long id);

    List<Category> selectAll();

    List<Category> selectList(@Param("name") String name, @Param("status") Integer status);

    int insert(Category category);

    int update(Category category);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}