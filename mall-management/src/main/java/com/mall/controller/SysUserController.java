package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.SysUser;
import com.mall.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class SysUserController {
    
    @Autowired
    private SysUserService sysUserService;
    
    @GetMapping("/list")
    public Result<List<SysUser>> list(SysUser user) {
        List<SysUser> list = sysUserService.getList(user);
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.success(user);
    }
    
    @PostMapping("/save")
    public Result<String> save(@RequestBody SysUser user) {
        boolean result = sysUserService.save(user);
        if (result) {
            return Result.success("保存成功");
        }
        return Result.error("用户名已存在");
    }
    
    @PostMapping("/update")
    public Result<String> update(@RequestBody SysUser user) {
        boolean result = sysUserService.update(user);
        if (result) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
    
    @PostMapping("/updateStatus")
    public Result<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        boolean result = sysUserService.updateStatus(id, status);
        if (result) {
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }
    
    @PostMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean result = sysUserService.deleteById(id);
        if (result) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
