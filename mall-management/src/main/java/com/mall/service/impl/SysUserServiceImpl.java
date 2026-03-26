package com.mall.service.impl;

import com.mall.entity.SysUser;
import com.mall.mapper.SysUserMapper;
import com.mall.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {
    
    private static final String USER_CACHE_PREFIX = "user:";
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public SysUser login(String username, String password) {
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        SysUser user = sysUserMapper.login(username, md5Password);
        if (user != null && user.getStatus() == 1) {
            redisTemplate.opsForValue().set(USER_CACHE_PREFIX + user.getId(), user, 30, TimeUnit.MINUTES);
            return user;
        }
        return null;
    }
    
    @Override
    public SysUser getById(Long id) {
        String key = USER_CACHE_PREFIX + id;
        SysUser user = (SysUser) redisTemplate.opsForValue().get(key);
        if (user == null) {
            user = sysUserMapper.selectById(id);
            if (user != null) {
                redisTemplate.opsForValue().set(key, user, 30, TimeUnit.MINUTES);
            }
        }
        return user;
    }
    
    @Override
    public SysUser getByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }
    
    @Override
    public List<SysUser> getList(SysUser user) {
        return sysUserMapper.selectList(user);
    }
    
    @Override
    public boolean save(SysUser user) {
        SysUser existUser = sysUserMapper.selectByUsername(user.getUsername());
        if (existUser != null) {
            return false;
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setStatus(1);
        return sysUserMapper.insert(user) > 0;
    }
    
    @Override
    public boolean update(SysUser user) {
        int result = sysUserMapper.update(user);
        if (result > 0) {
            redisTemplate.delete(USER_CACHE_PREFIX + user.getId());
        }
        return result > 0;
    }
    
    @Override
    public boolean updateStatus(Long id, Integer status) {
        int result = sysUserMapper.updateStatus(id, status);
        if (result > 0) {
            redisTemplate.delete(USER_CACHE_PREFIX + id);
        }
        return result > 0;
    }
    
    @Override
    public boolean deleteById(Long id) {
        int result = sysUserMapper.deleteById(id);
        if (result > 0) {
            redisTemplate.delete(USER_CACHE_PREFIX + id);
        }
        return result > 0;
    }
}
