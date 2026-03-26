package com.mall.mapper;

import com.mall.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    
    User selectById(Long id);
    
    User selectByUsername(String username);
    
    List<User> selectList(@Param("keyword") String keyword, @Param("status") Integer status);
    
    int insert(User user);
    
    int update(User user);
    
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    int deleteById(Long id);
    
    long count();
}
