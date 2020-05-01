
package com.bootx.entity;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 角色
 *
 * @author blackboy
 * @version 1.0
 */
@Entity
@Table(name = "bootx_role")
public class Role extends BaseEntity<Long> {

	private static final long serialVersionUID = -6614052029623997372L;

	/**
	 * 名称
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	@JsonView({TreeView.class,ListView.class,EditView.class})
	private String name;

	/**
	 * 是否内置
	 */
	@Column(nullable = false, updatable = false)
	@JsonView({ListView.class,EditView.class})
	private Boolean isSystem;

	/**
	 * 是否内置
	 */
	@Column(nullable = false, updatable = false)
	@JsonView({ListView.class,EditView.class})
	private Boolean isEnabled;

	/**
	 * 描述
	 */
	@Length(max = 200)
	@JsonView({ListView.class,EditView.class})
	private String description;

	/**
	 * 权限
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Permission> permissions = new HashSet<>();

	/**
	 * 管理员
	 */
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<Admin> admins = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	private Department department;

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
	 * 获取是否内置
	 *
	 * @return 是否内置
	 */
	public Boolean getIsSystem() {
		return isSystem;
	}

	/**
	 * 设置是否内置
	 *
	 * @param isSystem
	 *            是否内置
	 */
	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	/**
	 * 获取描述
	 *
	 * @return 描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 *
	 * @param description
	 *            描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取权限
	 *
	 * @return 权限
	 */
	public Set<Permission> getPermissions() {
		return permissions;
	}

	/**
	 * 设置权限
	 *
	 * @param permissions
	 *            权限
	 */
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * 获取管理员
	 *
	 * @return 管理员
	 */
	public Set<Admin> getAdmins() {
		return admins;
	}

	/**
	 * 设置管理员
	 *
	 * @param admins
	 *            管理员
	 */
	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Transient
	@JsonView({EditView.class})
	public Long getDepartmentId() {
		if(department!=null){
			return department.getId();
		}
		return null;
	}

	@Transient
	@JsonView({ListView.class, EditView.class})
	public String getDepartmentName() {
		if(department!=null){
			return department.getName();
		}
		return null;
	}

	public List<String> getPermissionUrls(){
		List<String> urls = new ArrayList<>();
		if(getPermissions()!=null && getPermissions().size()>0){
			for (Permission permission:getPermissions()) {
				urls.addAll(permission.getPermissions().values());
			}
		}
		return urls;
	}

}
