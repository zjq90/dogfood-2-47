package com.mall.interceptor;

import com.mall.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        
        if (user == null) {
            if (isAjaxRequest(request)) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(401);
                response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\"}");
            } else {
                response.sendRedirect("/login");
            }
            return false;
        }
        
        return true;
    }
    
    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }
}
