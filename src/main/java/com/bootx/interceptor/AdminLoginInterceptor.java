package com.bootx.interceptor;

import com.bootx.common.Message;
import com.bootx.entity.Admin;
import com.bootx.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class AdminLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) { System.out.println(request.getRequestURI());
        System.out.println(request.getRequestURL());
        Admin admin = adminService.getCurrent();
        if (admin == null) {
            response.setContentType("application/json");
            Map<String, Object> data = new HashMap<>();
            data.put("message", Message.error("请先登录"));
            response.setStatus(999);
            return false;
        }
        return true;
    }
}
