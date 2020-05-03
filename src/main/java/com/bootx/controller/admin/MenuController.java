
package com.bootx.controller.admin;

import com.bootx.common.Message;
import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.BaseEntity;
import com.bootx.entity.Menu;
import com.bootx.entity.Permission;
import com.bootx.service.MenuService;
import com.bootx.service.PermissionService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 菜单
 *
 * @author blackboy
 * @version 1.0
 */
@RestController("adminMenuController")
@RequestMapping("/api/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;
	@Autowired
	private PermissionService permissionService;

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public Message save(Menu menu, Long parentId) {
		menu.setParent(menuService.find(parentId));

		String menuUrl = menu.getUrl();
		if(StringUtils.startsWith(menuUrl,"/")){
			menuUrl = menuUrl.substring(1);
		}
		if(StringUtils.endsWith(menuUrl,"/")){
			menuUrl = menuUrl.substring(0,menuUrl.length()-1);
		}
		menu.setPermission(menuUrl.replaceAll("/",":"));

		if(menu.getIsEnabled()==null){
			menu.setIsEnabled(false);
		}
		if (!isValid(menu)) {
			return Message.error("参数错误");
		}
    menu.setTreePath(null);
    menu.setGrade(null);
    menu.setChildren(null);
    menuService.save(menu);
    refreshPermission(menu);

		return Message.success("操作成功");
	}


	private void refreshPermission(Menu menu){
	  // 添加
    savePermission(menu,"添加","/add",0);
    // 删除
    savePermission(menu,"删除","/delete",0);
    // 修改
    savePermission(menu,"修改","/update",0);
    // 查看
    savePermission(menu,"查看","/view",0);
    // 添加
    savePermission(menu,"添加","/add",1);
    // 删除
    savePermission(menu,"删除","/delete",1);
    // 修改
    savePermission(menu,"修改","/update",1);
    // 查看
    savePermission(menu,"查看","/view",1);

    // 添加
    savePermission(menu,"添加","/add",2);
    // 删除
    savePermission(menu,"删除","/delete",2);
    // 修改
    savePermission(menu,"修改","/update",2);
    // 查看
    savePermission(menu,"查看","/view",2);
  }

  private void savePermission(Menu menu,String name,String url2,Integer type){
    Permission permission = new Permission();
    permission.setName(name);
    permission.setIsEnabled(true);
    permission.setMemo(menu.getName()+permission.getName());
    permission.setMenu(menu);
    permission.setType(type);
    permission.setUrls(new ArrayList<>());
    permission.getUrls().add(menu.getUrl()+url2);

    Map<String,String> permissions = new HashMap<>();
    for (String url:permission.getUrls()) {
      String url1 = url;
      if(StringUtils.startsWith(url1,"/")){
        url1 = url1.substring(1);
      }
      if(StringUtils.endsWith(url1,"/")){
        url1 = url1.substring(0,url1.length()-1);
      }
      permissions.put(url,url1.replace("/",":")+"_"+permission.getType());
    }
    permission.setPermissions(permissions);
    permissionService.save(permission);
  }


	/**
	 * 编辑
	 */
	@PostMapping("/edit")
  @JsonView(BaseEntity.EditView.class)
	public Menu edit(Long id) {
		return menuService.find(id);
	}

  /**
   * 保存
   */
  @PostMapping("/update")
  public Message update(Menu menu, Long parentId) {
    menu.setParent(menuService.find(parentId));
    String menuUrl = menu.getUrl();
    if(StringUtils.startsWith(menuUrl,"/")){
      menuUrl = menuUrl.substring(1);
    }
    if(StringUtils.endsWith(menuUrl,"/")){
      menuUrl = menuUrl.substring(0,menuUrl.length()-1);
    }
    menu.setPermission(menuUrl.replaceAll("/",":"));

    if(menu.getIsEnabled()==null){
      menu.setIsEnabled(false);
    }
    if (!isValid(menu)) {
      return Message.error("参数错误");
    }
    menuService.update(menu,"treePath", "grade", "children","permissions");
    return Message.success("操作成功");
  }

	/**
	 * 列表
	 */
	@PostMapping("/list")
	@JsonView(Menu.ListView.class)
	public Page<Menu> list(Pageable pageable,Long parentId) {
    return menuService.findPage(menuService.find(parentId),pageable);
	}


	/**
	 * 列表
	 */
	@PostMapping("/tree")
	@JsonView(Menu.TreeView.class)
	public List<Menu> tree() {
		return menuService.findRoots();
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Menu menu = menuService.find(id);
				if (menu != null && CollectionUtils.isNotEmpty(menu.getPermissions())) {
					return Message.error("删除失败");
				}
			}
			menuService.delete(ids);
		}
		return Message.success("操作成功");
	}

}
