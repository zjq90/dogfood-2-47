package com.mall.controller;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/list")
    public String listPage() {
        return "user/list";
    }
    
    @GetMapping("/data")
    @ResponseBody
    public Result<PageResult<User>> getUserList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.getUserList(keyword, status, pageNum, pageSize);
    }
    
    @GetMapping("/add")
    public String addPage() {
        return "user/add";
    }
    
    @PostMapping("/add")
    @ResponseBody
    public Result<Void> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }
    
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Result<User> result = userService.getUserById(id);
        if (result.isSuccess()) {
            model.addAttribute("user", result.getData());
        }
        return "user/edit";
    }
    
    @PostMapping("/update")
    @ResponseBody
    public Result<Void> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
    
    @PostMapping("/updateStatus")
    @ResponseBody
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        return userService.updateUserStatus(id, status);
    }
    
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
    
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Result<User> getUserDetail(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
