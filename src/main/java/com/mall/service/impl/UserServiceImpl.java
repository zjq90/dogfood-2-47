package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.mapper.UserMapper;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String USER_SESSION_KEY = "user:session:";
    private static final String USER_CACHE_KEY = "user:cache:";
    
    @Override
    public Result<User> login(String username, String password, HttpSession session) {
        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        String encryptedPassword = encryptPassword(password);
        if (!encryptedPassword.equals(user.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        
        if (user.getStatus() == User.STATUS_DISABLED) {
            return Result.error("账号已被禁用，请联系管理员");
        }
        
        user.setPassword(null);
        session.setAttribute("currentUser", user);
        
        String sessionKey = USER_SESSION_KEY + session.getId();
        redisTemplate.opsForValue().set(sessionKey, user, 30, TimeUnit.MINUTES);
        
        String cacheKey = USER_CACHE_KEY + user.getId();
        redisTemplate.opsForValue().set(cacheKey, user, 60, TimeUnit.MINUTES);
        
        return Result.success("登录成功", user);
    }
    
    @Override
    public Result<Void> logout(HttpSession session) {
        String sessionKey = USER_SESSION_KEY + session.getId();
        redisTemplate.delete(sessionKey);
        session.invalidate();
        return Result.success("退出成功", null);
    }
    
    @Override
    public Result<User> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED, "未登录");
        }
        return Result.success(user);
    }
    
    @Override
    public Result<PageResult<User>> getUserList(String keyword, Integer status, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectList(keyword, status);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        
        PageResult<User> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo.getList(), 
                                                       pageNum, pageSize);
        return Result.success(pageResult);
    }
    
    @Override
    public Result<User> getUserById(Long id) {
        String cacheKey = USER_CACHE_KEY + id;
        User user = (User) redisTemplate.opsForValue().get(cacheKey);
        
        if (user == null) {
            user = userMapper.selectById(id);
            if (user != null) {
                user.setPassword(null);
                redisTemplate.opsForValue().set(cacheKey, user, 30, TimeUnit.MINUTES);
            }
        }
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }
    
    @Override
    public Result<Void> addUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        
        User existUser = userMapper.selectByUsername(user.getUsername());
        if (existUser != null) {
            return Result.error("用户名已存在");
        }
        
        user.setPassword(encryptPassword(user.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(User.STATUS_ENABLED);
        }
        if (user.getRole() == null) {
            user.setRole(User.ROLE_USER);
        }
        
        userMapper.insert(user);
        return Result.success("添加成功", null);
    }
    
    @Override
    public Result<Void> updateUser(User user) {
        if (user.getId() == null) {
            return Result.error("用户ID不能为空");
        }
        
        User existUser = userMapper.selectById(user.getId());
        if (existUser == null) {
            return Result.error("用户不存在");
        }
        
        userMapper.update(user);
        
        redisTemplate.delete(USER_CACHE_KEY + user.getId());
        
        return Result.success("更新成功", null);
    }
    
    @Override
    public Result<Void> updateUserStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return Result.error("参数错误");
        }
        if (status != User.STATUS_ENABLED && status != User.STATUS_DISABLED) {
            return Result.error("状态值无效");
        }
        
        userMapper.updateStatus(id, status);
        redisTemplate.delete(USER_CACHE_KEY + id);
        
        return Result.success("操作成功", null);
    }
    
    @Override
    public Result<Void> deleteUser(Long id) {
        userMapper.deleteById(id);
        redisTemplate.delete(USER_CACHE_KEY + id);
        return Result.success("删除成功", null);
    }
    
    @Override
    public Result<Void> changePassword(Long id, String oldPassword, String newPassword) {
        if (id == null || oldPassword == null || newPassword == null) {
            return Result.error("参数错误");
        }
        
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        if (!encryptPassword(oldPassword).equals(user.getPassword())) {
            return Result.error("原密码错误");
        }
        
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setPassword(encryptPassword(newPassword));
        userMapper.update(updateUser);
        
        redisTemplate.delete(USER_CACHE_KEY + id);
        
        return Result.success("密码修改成功", null);
    }
    
    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }
}
