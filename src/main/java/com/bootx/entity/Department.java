
package com.bootx.entity;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bootx_department")
public class Department extends OrderedEntity<Long> {

	private static final long serialVersionUID = 5095521437302782717L;


	/**
	 * 树路径分隔符
	 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/**
	 * 名称
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	@JsonView({ListView.class,TreeView.class})
	private String name;

	/**
	 * 树路径
	 */
	@Column(nullable = false)
	private String treePath;

	/**
	 * 层级
	 */
	@Column(nullable = false)
	private Integer grade;

	/**
	 * 上级部门
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Department parent;

	@JsonView({ListView.class})
	private Boolean isEnabled;

	@OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
	private Set<Admin> admins = new HashSet<>();

	@OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
	private Set<Role> roles = new HashSet<>();

	/**
	 * 下级部门
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	@JsonView({TreeView.class})
	private Set<Department> children = new HashSet<>();

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取树路径
	 * 
	 * @return 树路径
	 */
	public String getTreePath() {
		return treePath;
	}

	/**
	 * 设置树路径
	 * 
	 * @param treePath
	 *            树路径
	 */
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	/**
	 * 获取层级
	 * 
	 * @return 层级
	 */
	public Integer getGrade() {
		return grade;
	}

	/**
	 * 设置层级
	 * 
	 * @param grade
	 *            层级
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	/**
	 * 获取上级部门
	 * 
	 * @return 上级部门
	 */
	public Department getParent() {
		return parent;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 设置上级部门
	 * 
	 * @param parent
	 *            上级部门
	 */
	public void setParent(Department parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级部门
	 * 
	 * @return 下级部门
	 */
	public Set<Department> getChildren() {
		return children;
	}

	/**
	 * 设置下级部门
	 * 
	 * @param children
	 *            下级部门
	 */
	public void setChildren(Set<Department> children) {
		this.children = children;
	}


	public Set<Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * 获取所有上级分类ID
	 * 
	 * @return 所有上级分类ID
	 */
	@Transient
	public Long[] getParentIds() {
		String[] parentIds = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
		Long[] result = new Long[parentIds.length];
		for (int i = 0; i < parentIds.length; i++) {
			result[i] = Long.valueOf(parentIds[i]);
		}
		return result;
	}

	/**
	 * 获取所有上级部门
	 * 
	 * @return 所有上级部门
	 */
	@Transient
	public List<Department> getParents() {
		List<Department> parents = new ArrayList<>();
		recursiveParents(parents, this);
		return parents;
	}

	/**
	 * 递归上级部门
	 * 
	 * @param parents
	 *            上级部门
	 * @param menu
	 *            部门
	 */
	private void recursiveParents(List<Department> parents, Department menu) {
		if (menu == null) {
			return;
		}
		Department parent = menu.getParent();
		if (parent != null) {
			parents.add(0, parent);
			recursiveParents(parents, parent);
		}
	}


	@Transient
	@JsonView({ListView.class})
	public Long getParentId(){
		if(parent!=null){
			return parent.getId();
		}
		return null;
	}


	@Transient
	@JsonView({ListView.class})
	public String getParentName(){
		if(parent!=null){
			return parent.getName();
		}
		return null;
	}


	public interface TreeView extends IdView{}
	public interface ListView extends BaseView{}
}