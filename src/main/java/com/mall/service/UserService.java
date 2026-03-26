package com.mall.service;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    
    Result<User> login(String username, String password, HttpSession session);
    
    Result<Void> logout(HttpSession session);
    
    Result<User> getCurrentUser(HttpSession session);
    
    Result<PageResult<User>> getUserList(String keyword, Integer status, Integer pageNum, Integer pageSize);
    
    Result<User> getUserById(Long id);
    
    Result<Void> addUser(User user);
    
    Result<Void> updateUser(User user);
    
    Result<Void> updateUserStatus(Long id, Integer status);
    
    Result<Void> deleteUser(Long id);
    
    Result<Void> changePassword(Long id, String oldPassword, String newPassword);
}
