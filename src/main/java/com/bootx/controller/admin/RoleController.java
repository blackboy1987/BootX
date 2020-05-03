
package com.bootx.controller.admin;

import com.bootx.common.Message;
import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.BaseEntity;
import com.bootx.entity.Menu;
import com.bootx.entity.Permission;
import com.bootx.entity.Role;
import com.bootx.service.DepartmentService;
import com.bootx.service.MenuService;
import com.bootx.service.PermissionService;
import com.bootx.service.RoleService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Controller - 管理员
 *
 * @author blackboy
 * @version 1.0
 */
@RestController
@RequestMapping("/api/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;
  @Autowired
  private DepartmentService departmentService;
  @Autowired
  private MenuService menuService;
  @Autowired
  private PermissionService permissionService;

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public Message save(Role role,Long departmentId) {
	  role.setDepartment(departmentService.find(departmentId));
    Map<String,String> validResults = isValid1(role, BaseEntity.Save.class);
    if(!validResults.isEmpty()){
      return Message.error1("参数错误",validResults);
    }
    roleService.save(role);
    return Message.success("添加成功");

	}

	/**
	 * 编辑
	 */
	@PostMapping("/edit")
	@JsonView(BaseEntity.EditView.class)
	public Role edit(Long id) {
		return roleService.find(id);
	}

  /**
   * 保存
   */
  @PostMapping("/update")
  public Message update(Role role,Long departmentId) {
    role.setDepartment(departmentService.find(departmentId));
    Map<String,String> validResults = isValid1(role, BaseEntity.Save.class);
    if(!validResults.isEmpty()){
      return Message.error1("参数错误",validResults);
    }
    roleService.update(role,"admins");
    return Message.success("添加成功");

  }

	/**
	 * 列表
	 */
	@PostMapping("/list")
	@JsonView(BaseEntity.ListView.class)
	public Page<Role> list(Pageable pageable,String name,Date beginDate,Date endDate) {
		return roleService.findPage(pageable);
	}


	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public Message delete(Long[] ids) {
		for (Long id:ids){
		  Role role = roleService.find(id);
		  if(role!=null){
        if (CollectionUtils.isNotEmpty(role.getAdmins())) {
          return Message.error("删除失败");
        }
        roleService.delete(role);
      }
    }
		return Message.success("删除成功");
	}

  /**
   * 列表
   */
  @PostMapping("/all")
  @JsonView(BaseEntity.TreeView.class)
  public List<Role> all() {
    return roleService.findAll();
  }

  @PostMapping("/permission")
  @JsonView(Menu.PermissionView.class)
  public Map<String,Object> permission(Long roleId){
    Map<String,Object> map = new HashMap<>();
    List<Menu> menus = menuService.findRoots();
    List<String> permissionIds = new ArrayList<>();
    List<Long> menuList = new ArrayList<>();
    Role role = roleService.find(roleId);
    if(role!=null){
      for (Permission permission:role.getPermissions()) {
        permissionIds.add(permission.getMenuId()+"_"+permission.getId()+"_"+permission.getType());
      }
      for(Menu menu : role.getMenus()){
        menuList.add(menu.getId());
      }
    }
    map.put("menuTree",menus);
    map.put("menuList",menuList);
    map.put("permissionIds",permissionIds);
    return map;
  }

  @PostMapping("/update_permission")
  public Message updatePermission(Long roleId,String[] buttonList,String[] dataList,String [] interfaceList,Long[] menuList){
    List<Long> permissionIds = new ArrayList<>();
    Role role = roleService.find(roleId);
    if(role==null){
      return Message.error("角色不存在");
    }
    if(buttonList!=null&&buttonList.length>0){
      for (String button:buttonList) {
        String[] buttons = button.split("_");
        if(buttons.length==3){
          permissionIds.add(Long.valueOf(buttons[1]));
        }
      }
    }
    if(dataList!=null&&dataList.length>0){
      for (String data:dataList) {
        String[] buttons = data.split("_");
        if(buttons.length==3){
          permissionIds.add(Long.valueOf(buttons[1]));
        }
      }
    }
    if(interfaceList!=null&&interfaceList.length>0){
      for (String interface1:interfaceList) {
        String[] buttons = interface1.split("_");
        if(buttons.length==3){
          permissionIds.add(Long.valueOf(buttons[1]));
        }
      }
    }
    if(permissionIds.isEmpty()){
      role.setPermissions(new HashSet<>());
    }else{
      role.setPermissions(new HashSet<>(permissionService.findList(permissionIds.toArray(new Long[permissionIds.size()]))));
    }
    role.setMenus(new HashSet<>(menuService.findList(menuList)));
    roleService.update(role);
    return Message.success("操作成功");
  }


  @PostMapping("/permission_view")
  public List<Long> permissionView(Long roleId) {
    List<Long> permissionIds = new ArrayList<>();
    Role role = roleService.find(roleId);
    if(role!=null){
      for (Permission permission:role.getPermissions()) {
        permissionIds.add(permission.getId());
      }
    }
    return permissionIds;
  }
}
