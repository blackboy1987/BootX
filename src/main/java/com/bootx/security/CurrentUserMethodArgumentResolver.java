
package com.bootx.security;

import com.bootx.entity.User;
import com.bootx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Security - 当前用户MethodArgumentResolver
 * 
 * @author blackboy
 * @version 1.0
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private UserService userService;

	/**
	 * 支持参数
	 * 
	 * @param methodParameter
	 *            MethodParameter
	 * @return 是否支持参数
	 */
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.hasParameterAnnotation(CurrentUser.class) && User.class.isAssignableFrom(methodParameter.getParameterType());
	}

	/**
	 * 解析变量
	 * 
	 * @param methodParameter
	 *            MethodParameter
	 * @param modelAndViewContainer
	 *            ModelAndViewContainer
	 * @param nativeWebRequest
	 *            NativeWebRequest
	 * @param webDataBinderFactory
	 *            WebDataBinderFactory
	 * @return 变量
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		return userService.getCurrent((Class<User>) methodParameter.getParameterType());
	}

}