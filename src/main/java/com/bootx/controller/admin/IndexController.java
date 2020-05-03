
package com.bootx.controller.admin;

import com.bootx.entity.Admin;
import com.bootx.entity.Menu;
import com.bootx.entity.User;
import com.bootx.service.AdminService;
import com.bootx.service.MenuService;
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


	/**
	 * 菜单权限
	 */
	@PostMapping("/auth_routes")
  @JsonView(Menu.AuthRoutesView.class)
	public Map<String,Object> authRoutes() {
		Map<String,Object> data = new HashMap<>();
    Admin admin = adminService.getCurrent();
    Set<Menu> menus = adminService.getMenus(admin);
    for (Menu menu:menus) {
      menus.addAll(menuService.findParents(menu,true,null));
    }
    data.put("menus",buildTree(menus));
		return data;
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
