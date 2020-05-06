
package com.bootx.controller.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.bootx.common.Message;
import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Permission;
import com.bootx.service.MenuService;
import com.bootx.service.PermissionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 权限
 *
 * @author blackboy
 * @version 1.0
 */
@RestController("adminPermissionController")
@RequestMapping("/api/permission")
public class PermissionController extends BaseController {

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private ShiroFilterFactoryBean shiroFilterFactoryBean;

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public Message save(Permission permission,Long menuId) {
		permission.setMenu(menuService.find(menuId));
		if(permission.getIsEnabled()==null){
			permission.setIsEnabled(false);
		}

		if(permissionService.exists(permission)){
			return Message.error("权限已存在");
		}

		Map<String,String> permissions = new HashMap<>();
		for (String url:permission.getUrls()) {
			String url1 = url;
			if(StringUtils.startsWith(url1,"/")){
				url1 = url1.substring(1);
			}
			if(StringUtils.endsWith(url1,"/")){
				url1 = url1.substring(0,url1.length()-1);
			}
			permissions.put(url,url1.replace("/",":"));
		}
		permission.setPermissions(permissions);
		if (!isValid(permission)) {
			return Message.error("参数错误");
		}



		if(permission.isNew()){
			permissionService.save(permission);
		}else{
			permissionService.update(permission,"menu");
		}

		updatePermission();

		return Message.success("操作成功");
	}


	private void updatePermission() {
		synchronized (shiroFilterFactoryBean) {
			AbstractShiroFilter shiroFilter;
			try {
				shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
			} catch (Exception e) {
				throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
			}
			PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver)    shiroFilter.getFilterChainResolver();
			DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
			// 清空老的权限控制
			manager.getFilterChains().clear();
			shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();

			// 刷新权限配置
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
			filterChainDefinitionMap.put("/admin/**","adminAuthc");
			shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
			// 重新构建生成
			Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
			for (Map.Entry<String, String> entry : chains.entrySet()) {
				String url = entry.getKey();
				String chainDefinition = entry.getValue().trim().replace(" ", "");
				manager.createChain(url, chainDefinition);
			}
		}
	}



	/**
	 * 编辑
	 */
	@PostMapping("/edit")
	public Permission edit(Long id) {
		return permissionService.find(id);
	}

	/**
	 * 列表
	 */
	@PostMapping("/list")
	@JsonView(Permission.ListView.class)
	public Page<Permission> list(Pageable pageable,Long menuId) {
		return permissionService.findPage(pageable,menuService.find(menuId));
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public Message delete(Long[] ids) {
		permissionService.delete(ids);
		return Message.success("操作成功");
	}

	/**
	 * 列表
	 */
	@PostMapping("/getAll")
	public List<Permission> getAll() {
		return permissionService.findAll();
	}

}
