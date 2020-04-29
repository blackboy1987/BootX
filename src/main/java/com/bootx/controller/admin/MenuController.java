
package com.bootx.controller.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.bootx.common.Message;
import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Menu;
import com.bootx.service.MenuService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller - 菜单
 * 
 * @author blackboy
 * @version 1.0
 */
@RestController("adminMenuController")
@RequestMapping("/admin/api/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;

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
		if(menu.isNew()){
			menu.setTreePath(null);
			menu.setGrade(null);
			menu.setChildren(null);
			menuService.save(menu);
		}else{
			menuService.update(menu,"children");
		}

		return Message.success("操作成功");
	}

	/**
	 * 编辑
	 */
	@PostMapping("/edit")
	public Menu edit(Long id) {
		return menuService.find(id);
	}

	/**
	 * 列表
	 */
	@PostMapping("/list")
	@JsonView(Menu.ListView.class)
	public Page<Menu> list(Pageable pageable,Long parentId) {
		pageable.setPageSize(5000);
		List<Menu> menus;
		if(parentId==null){
			menus = menuService.findRoots();
		}else {
			menus = menuService.findChildren(menuService.find(parentId),false,null);
		}
		return new Page(menus,menus.size(),pageable);

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