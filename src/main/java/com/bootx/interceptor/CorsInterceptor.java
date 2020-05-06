package com.bootx.interceptor;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Interceptor - 跨域拦截器
 *
 * @author blackboy
 * @version 1.0
 */
public class CorsInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

	  response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x_requested_with,x-requested-with,Authorization,Content-Type,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");


    // 如果是OPTIONS则结束请求
    if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
      response.setStatus(HttpStatus.NO_CONTENT.value());
      return false;
    }

		return true;
	}
}
