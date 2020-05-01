
package com.bootx.controller.admin;

import com.bootx.common.Message;
import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.BaseEntity;
import com.bootx.entity.Role;
import com.bootx.service.RoleService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public Message save(Role role) {
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
}
