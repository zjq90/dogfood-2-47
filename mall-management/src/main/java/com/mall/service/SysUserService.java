package com.mall.service;

import com.mall.entity.SysUser;
import java.util.List;

public interface SysUserService {
    
    SysUser login(String username, String password);
    
    SysUser getById(Long id);
    
    SysUser getByUsername(String username);
    
    List<SysUser> getList(SysUser user);
    
    boolean save(SysUser user);
    
    boolean update(SysUser user);
    
    boolean updateStatus(Long id, Integer status);
    
    boolean deleteById(Long id);
}
