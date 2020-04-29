package com.bootx.config;

import com.bootx.entity.Admin;
import com.bootx.entity.Permission;
import com.bootx.security.AuthenticationFilter;
import com.bootx.security.AuthorizingRealm;
import com.bootx.security.LogoutFilter;
import com.bootx.service.PermissionService;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    private EhCacheManagerFactoryBean ehCacheManagerFactoryBean;

    @Autowired
    private PermissionService permissionService;

    @Bean
    public ShiroFilterFactoryBean shiroFilter(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        // shiroFilterFactoryBean.setUnauthorizedUrl("/common/error/unauthorized");

        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/admin","anon");
        filterChainDefinitionMap.put("/admin/","anon");
        filterChainDefinitionMap.put("/admin/currentUser","anon");
        filterChainDefinitionMap.put("/admin/login","adminAuthc");
        filterChainDefinitionMap.put("/admin/logout","logout");


        List<Permission> permissions = permissionService.findAll();
        for (Permission permission:permissions) {
            Map<String,String> permissions1 = permission.getPermissions();
            for (String key:permissions1.keySet()) {
                filterChainDefinitionMap.put(key,"adminAuthc,perms["+permissions1.get(key)+"]");
            }
        }
       // filterChainDefinitionMap.put("/admin/**","adminAuthc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter > filters = new HashMap<>();

        filters.put("adminAuthc",adminAuthc());

        filters.put("logout",new LogoutFilter());
        shiroFilterFactoryBean.setFilters(filters);

        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(authorizingRealm());
        defaultWebSecurityManager.setCacheManager(shiroCacheManager());
        return defaultWebSecurityManager;
    }

    @Bean
    public AuthorizingRealm authorizingRealm(){
        AuthorizingRealm authorizingRealm = new AuthorizingRealm();
        authorizingRealm.setAuthenticationCacheName("authorization");
        return authorizingRealm;
    }

    @Bean
    public EhCacheManager shiroCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(ehCacheManagerFactoryBean.getObject());
        return ehCacheManager;
    }

    @Bean
    public AuthenticationFilter adminAuthc(){
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setUserClass(Admin.class);
        authenticationFilter.setLoginUrl("/admin/login");
        authenticationFilter.setSuccessUrl("/admin/index");
        return authenticationFilter;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean (){
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(securityManager());

        return methodInvokingFactoryBean;
    }
}