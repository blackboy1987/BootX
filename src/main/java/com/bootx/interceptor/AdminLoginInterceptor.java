package com.bootx.interceptor;

import com.bootx.common.Message;
import com.bootx.entity.Admin;
import com.bootx.service.UserService;
import com.bootx.util.JWTUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class AdminLoginInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
		Admin admin = userService.getCurrent(Admin.class);
    try{
      Claims claims = JWTUtils.parseToken(request.getHeader("Authorization"));
    }catch (Exception e){
      response.setContentType("application/json");
      Map<String, Object> data = new HashMap<>();
      data.put("message", Message.error("请先登录"));
      response.setStatus(999);
      return false;
    }
		if(admin==null){
			response.setContentType("application/json");
			Map<String, Object> data = new HashMap<>();
			data.put("message", Message.error("请先登录"));
			response.setStatus(999);
			return false;
		}
		return true;
	}
}
