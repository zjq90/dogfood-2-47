package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.SysUser;
import com.mall.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {
    
    @Autowired
    private SysUserService sysUserService;
    
    @GetMapping("/")
    public String index() {
        return "login";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/api/login")
    @ResponseBody
    public Result<SysUser> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        SysUser user = sysUserService.login(username, password);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        if (user.getStatus() != 1) {
            return Result.error("账号已被停用");
        }
        session.setAttribute("user", user);
        return Result.success("登录成功", user);
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }
}
