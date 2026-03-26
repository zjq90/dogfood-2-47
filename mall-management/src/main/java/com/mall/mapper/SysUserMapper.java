package com.mall.mapper;

import com.mall.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserMapper {
    
    SysUser selectById(Long id);
    
    SysUser selectByUsername(String username);
    
    List<SysUser> selectList(SysUser user);
    
    int insert(SysUser user);
    
    int update(SysUser user);
    
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    int deleteById(Long id);
    
    SysUser login(@Param("username") String username, @Param("password") String password);
}
