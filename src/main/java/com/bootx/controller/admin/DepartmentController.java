
package com.bootx.controller.admin;

import com.bootx.common.Message;
import com.bootx.entity.BaseEntity;
import com.bootx.entity.Department;
import com.bootx.service.DepartmentService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller - 部门
 *
 * @author blackboy
 * @version 1.0
 */
@RestController("adminDepartmentController")
@RequestMapping("/api/department")
public class DepartmentController extends BaseController {

	@Autowired
	private DepartmentService departmentService;

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public Message save(Department department, Long parentId) {
		department.setParent(departmentService.find(parentId));
		if(department.getIsEnabled()==null){
			department.setIsEnabled(false);
		}
		if (!isValid(department)) {
			return Message.error("参数错误");
		}
    department.setTreePath(null);
    department.setGrade(null);
    department.setChildren(null);
    department.setAdmins(null);
    departmentService.save(department);
		return Message.success("操作成功");
	}

	/**
	 * 编辑
	 */
	@PostMapping("/edit")
  @JsonView(BaseEntity.EditView.class)
	public Department edit(Long id) {
		return departmentService.find(id);
	}

  /**
   * 更新
   */
  @PostMapping("/update")
  public Message update(Department department, Long parentId) {
    department.setParent(departmentService.find(parentId));
    if (!isValid(department)) {
      return Message.error("参数错误");
    }
    if (department.getParent() != null) {
      Department parent = department.getParent();
      if (parent.equals(department)) {
        return Message.error("参数错误");
      }
      List<Department> children = departmentService.findChildren(parent, true, null);
      if (children != null && children.contains(parent)) {
        return Message.error("参数错误");
      }
    }
    departmentService.update(department, "treePath", "grade", "children", "admins", "roles");
    return Message.success("更新成功");
  }

	/**
	 * 列表
	 */
	@PostMapping("/list")
	@JsonView(Department.ListView.class)
	public List<Department> list(Long parentId) {
		if(parentId==null){
			return departmentService.findRoots();
		}
		return departmentService.findChildren(departmentService.find(parentId),false,null);
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Department department = departmentService.find(id);
				if (department != null && CollectionUtils.isNotEmpty(department.getAdmins())) {
					return Message.error("删除失败");
				}
			}
			departmentService.delete(ids);
		}
		return Message.success("操作成功");
	}

	/**
	 * 列表
	 */
	@PostMapping("/tree")
	@JsonView(Department.TreeView.class)
	public List<Department> tree() {
		return departmentService.findRoots();
	}

}
