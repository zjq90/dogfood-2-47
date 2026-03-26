package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }
    
    @PostMapping("/doLogin")
    @ResponseBody
    public Result<User> doLogin(@RequestParam String username, 
                                @RequestParam String password,
                                HttpSession session) {
        return userService.login(username, password, session);
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "redirect:/login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        return "dashboard";
    }
}
