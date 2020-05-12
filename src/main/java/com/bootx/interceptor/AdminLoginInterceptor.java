package com.bootx.interceptor;

import com.bootx.common.Message;
import com.bootx.entity.Admin;
import com.bootx.service.AdminService;
import com.bootx.service.UserService;
import com.bootx.util.JWTUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class AdminLoginInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserService userService;

  @Autowired
  private AdminService adminService;

  @Autowired
  private RedisTemplate<String,String> redisTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
    System.out.println(request.getRequestURI());

	  Admin admin = adminService.getCurrent();
    try{
      Claims claims = JWTUtils.parseToken(request.getHeader("Authorization"));
      String token = redisTemplate.opsForValue().get(claims.getId());
      System.out.println(token);
      System.out.println(request.getHeader("Authorization"));

      if(admin==null){
        admin = adminService.find(Long.valueOf(claims.getId()));
      }
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
