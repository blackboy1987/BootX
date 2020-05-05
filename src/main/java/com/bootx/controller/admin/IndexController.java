
package com.bootx.controller.admin;

import com.bootx.entity.Admin;
import com.bootx.entity.Menu;
import com.bootx.entity.Permission;
import com.bootx.entity.User;
import com.bootx.security.CurrentUser;
import com.bootx.service.AdminService;
import com.bootx.service.MenuService;
import com.bootx.service.PermissionService;
import com.bootx.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * Controller - 登陆
 *
 * @author 夏黎
 * @version 1.0
 */
@RestController("adminIndexController")
@RequestMapping("/api")
public class IndexController extends BaseController {

  @Autowired
  private AdminService adminService;

  @Autowired
  private MenuService menuService;

  @Autowired
  private PermissionService permissionService;

  /**
   * 当前登陆信息
   */
  @PostMapping("/currentUser")
  public Map<String,Object> currentUser(@CurrentUser Admin admin) {
    Map<String,Object> data = new HashMap<>();
    data.put("username",admin.getUsername());
    data.put("id",admin.getId());
    data.put("departmentName",admin.getDepartmentName());
    data.put("name",admin.getName());
    data.put("roleNames",admin.getRoleNames());
    data.put("currentAuthority",authButtons());
    return data;
  }

	/**
	 * 菜单权限
	 */
	@PostMapping("/auth_routes")
  @JsonView(Menu.AuthRoutesView.class)
	public Map<String,Object> authRoutes() {
		Map<String,Object> data = new HashMap<>();
    Admin admin = adminService.getCurrent();
    if(admin==null){
      data.put("menus",Collections.emptyList());
      return data;
    }
    Set<Menu> menus = adminService.getMenus(admin);
    for (Menu menu:menus) {
      menus.addAll(menuService.findParents(menu,true,null));
    }
    data.put("menus",buildTree(menus));
		return data;
	}

  /**
   * 菜单权限
   */
  @PostMapping("/auth_buttons")
  public Set<String> authButtons() {
    Set<String> buttons = new HashSet<>();
    // 获取不需要校验的
    Menu root = new Menu();
    root.setId(-1L);
    List<Permission> permissions = permissionService.findList(0,false,true,root);
    for (Permission permission:permissions) {
      buttons.addAll(permission.getUrls());
    }
    Admin admin = adminService.getCurrent();
    if(admin!=null){
      Set<Menu> menus = adminService.getMenus(admin);
      for (Menu menu:menus) {
        permissions = permissionService.findList(0,true,true,menu);
        for (Permission permission:permissions) {
          buttons.addAll(permission.getUrls());
        }
      }
    }
    return buttons;
  }


	private List<Menu> buildTree(Set<Menu> menus){
	  List<Menu> result = new ArrayList<>();
    // 只存放根菜单
    for (Menu menu:menus) {
      if(menu.getGrade()==0){
        menu.setChildren(filter(menu.getChildren(),menus));
        result.add(menu);
      }
    }
    return result;
  }


  private Set<Menu> filter(Set<Menu> children,Set<Menu> menus){
    Set<Menu> result = new HashSet<>();
    for (Menu child:children) {
      if(menus.contains(child)){
        if(child.getChildren()!=null&&child.getChildren().size()>0){
          return filter(child.getChildren(),menus);
        }
        result.add(child);
      }
    }

    return result;
  }
}
