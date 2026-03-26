package com.mall.mapper;

import com.mall.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectByUsername(@Param("username") String username);

    User selectById(@Param("id") Long id);

    List<User> selectList(@Param("username") String username, @Param("status") Integer status);

    int insert(User user);

    int update(User user);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}