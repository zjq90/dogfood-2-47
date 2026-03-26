package com.mall.service;

import com.mall.entity.User;
import com.mall.common.Result;
import java.util.List;

public interface UserService {
    Result<User> login(String username, String password);

    Result<User> getUserById(Long id);

    Result<List<User>> getUserList(String username, Integer status);

    Result<String> addUser(User user);

    Result<String> updateUser(User user);

    Result<String> updateUserStatus(Long id, Integer status);

    Result<String> deleteUser(Long id);
}