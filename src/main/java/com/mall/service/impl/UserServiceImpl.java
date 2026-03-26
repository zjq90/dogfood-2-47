package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.mapper.UserMapper;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "user:";
    private static final long REDIS_EXPIRE_TIME = 30;

    @Override
    public Result<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(user.getPassword())) {
            return Result.error("密码错误");
        }
        if (user.getStatus() == 0) {
            return Result.error("用户已被禁用");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    @Override
    public Result<User> getUserById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        User user = (User) redisTemplate.opsForValue().get(redisKey);
        if (user == null) {
            user = userMapper.selectById(id);
            if (user != null) {
                redisTemplate.opsForValue().set(redisKey, user, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        return Result.success(user);
    }

    @Override
    public Result<List<User>> getUserList(String username, Integer status) {
        List<User> userList = userMapper.selectList(username, status);
        return Result.success(userList);
    }

    @Override
    public Result<String> addUser(User user) {
        User existUser = userMapper.selectByUsername(user.getUsername());
        if (existUser != null) {
            return Result.error("用户名已存在");
        }
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        int result = userMapper.insert(user);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result<String> updateUser(User user) {
        int result = userMapper.update(user);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + user.getId();
            redisTemplate.delete(redisKey);
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @Override
    public Result<String> updateUserStatus(Long id, Integer status) {
        int result = userMapper.updateStatus(id, status);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }

    @Override
    public Result<String> deleteUser(Long id) {
        int result = userMapper.deleteById(id);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}