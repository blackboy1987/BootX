
package com.bootx.interceptor;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.bootx.common.LogConfig;
import com.bootx.entity.Admin;
import com.bootx.entity.Log;
import com.bootx.entity.User;
import com.bootx.service.AdminService;
import com.bootx.service.LogConfigService;
import com.bootx.service.LogService;
import com.bootx.service.UserService;
import com.bootx.util.IpUtil;
import com.bootx.util.UserAgentUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LogInterceptor extends HandlerInterceptorAdapter {

	private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[] { "password", "rePassword", "currentPassword" };

	private static AntPathMatcher antPathMatcher = new AntPathMatcher();

	private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

	@Autowired
	private LogConfigService logConfigService;
	@Autowired
	private LogService logService;
	@Autowired
	private UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
    return true;
  }


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    List<LogConfig> logConfigs = logConfigService.getAll();
		if (logConfigs != null) {
			String path = request.getServletPath();
			for (LogConfig logConfig : logConfigs) {
				if (antPathMatcher.match(logConfig.getUrlPattern(), path)) {
          Log log = new Log();
          log.setAction(request.getRequestURI());
				  User user = userService.getCurrent();
				  if(user instanceof Admin){
            log.setOperator(((Admin)user).getUsername());
          }
					String operation = logConfig.getOperation();
					String content = (String) request.getAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
					String ip = IpUtil.getIpAddr(request);
					request.removeAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
					StringBuilder parameter = new StringBuilder();
					Map<String, String[]> parameterMap = request.getParameterMap();
					if (parameterMap != null) {
						for (Entry<String, String[]> entry : parameterMap.entrySet()) {
							String parameterName = entry.getKey();
							if (!ArrayUtils.contains(ignoreParameters, parameterName)) {
								String[] parameterValues = entry.getValue();
								if (parameterValues != null) {
									for (String parameterValue : parameterValues) {
										parameter.append(parameterName).append(" = ").append(parameterValue).append("\n");
									}
								}
							}
						}
					}
					log.setClientInfo(UserAgentUtils.getClientInfo(request.getHeader("user-agent")));
					log.setOperation(operation);
					log.setContent(content);
					log.setParameter(parameter.toString());
					log.setIp(ip);
					logService.save(log);
					break;
				}
			}
		}
	}

	public String[] getIgnoreParameters() {
		return ignoreParameters;
	}

	public void setIgnoreParameters(String[] ignoreParameters) {
		this.ignoreParameters = ignoreParameters;
	}


  /**
   * This implementation is empty.
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex){
    //System.out.println("afterCompletion==============3");
  }

  /**
   * This implementation is empty.
   */
  @Override
  public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler){
    System.out.println("afterConcurrentHandlingStarted==============4");
  }

}
