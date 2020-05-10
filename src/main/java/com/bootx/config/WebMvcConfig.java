package com.bootx.config;

import com.bootx.audit.AuditLogMethodArgumentResolver;
import com.bootx.entity.Admin;
import com.bootx.interceptor.AdminLoginInterceptor;
import com.bootx.interceptor.CorsInterceptor;
import com.bootx.interceptor.LogInterceptor;
import com.bootx.security.CurrentUserHandlerInterceptor;
import com.bootx.security.CurrentUserMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public CorsInterceptor corsInterceptor() {
        CorsInterceptor corsInterceptor = new CorsInterceptor();
        return corsInterceptor;
    }

    @Bean
    public AdminLoginInterceptor adminLoginInterceptor() {
        AdminLoginInterceptor adminLoginInterceptor = new AdminLoginInterceptor();
        return adminLoginInterceptor;
    }

  @Bean
  public LogInterceptor logInterceptor() {
    LogInterceptor logInterceptor = new LogInterceptor();
    return logInterceptor;
  }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       // registry.addInterceptor(corsInterceptor()).addPathPatterns("/**");
      registry.addInterceptor(logInterceptor())
        .addPathPatterns("/**");

        registry.addInterceptor(adminLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/logout","/setting/edit","/captcha/**","/auth_routes");

        registry.addInterceptor(currentUserHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/auth_routes");

    }


    @Bean
    public FixedLocaleResolver localeResolver(){
        FixedLocaleResolver localeResolver = new FixedLocaleResolver();
        return localeResolver;
    }

    @Bean
    public CurrentUserHandlerInterceptor currentUserHandlerInterceptor() {
        CurrentUserHandlerInterceptor currentUserHandlerInterceptor = new CurrentUserHandlerInterceptor();
        currentUserHandlerInterceptor.setUserClass(Admin.class);
        return currentUserHandlerInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>();
        handlerMethodArgumentResolvers.add(currentUserMethodArgumentResolver());
        handlerMethodArgumentResolvers.add(auditLogMethodArgumentResolver());
        resolvers.addAll(handlerMethodArgumentResolvers);
    }

    @Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver(){

        return new CurrentUserMethodArgumentResolver();
    }

    @Bean
    public AuditLogMethodArgumentResolver auditLogMethodArgumentResolver(){

        return new AuditLogMethodArgumentResolver();
    }

}
