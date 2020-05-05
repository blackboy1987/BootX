
package com.bootx.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 菜单
 *
 * @author blackboy
 * @version 1.0
 */
@Entity
@Table(name = "bootx_menu")
public class Menu extends OrderedEntity<Long> {

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
	@JsonView({ListView.class,TreeView.class,PermissionView.class,EditView.class,AuthRoutesView.class})
	private String name;

  @NotEmpty
  @Length(max = 200)
  @Column(nullable = false)
  @JsonView({ListView.class,EditView.class,AuthRoutesView.class})
	private String menuKey;

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
	 * 上级菜单
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Menu parent;

	@JsonView({ListView.class,EditView.class})
	private Boolean isEnabled;

	/**
	 * 下级菜单
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	@JsonView({TreeView.class,PermissionView.class,AuthRoutesView.class})
	private Set<Menu> children = new HashSet<>();

	/**
	 * 权限
	 */
	@OneToMany(mappedBy = "menu",fetch = FetchType.LAZY)
  @JsonView({PermissionView.class})
	private Set<Permission> permissions = new HashSet<>();

  @ManyToMany(mappedBy = "menus",fetch = FetchType.LAZY)
  private Set<Role> roles = new HashSet<>();

	/**
	 * 菜单跳转路径
	 */
	@NotEmpty
	@JsonView({ListView.class,EditView.class,AuthRoutesView.class})
  @JsonProperty("path")
	private String url;

	/**
	 * 菜单的权限名称
	 */
	@NotEmpty
	@JsonView({ListView.class})
	private String permission;

  @JsonView({ListView.class,EditView.class,AuthRoutesView.class})
	private String icon;

  @JsonView({ListView.class,EditView.class,AuthRoutesView.class})
	private String target;


  @JsonView({ListView.class,EditView.class,AuthRoutesView.class})
  private String component;


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

  public String getMenuKey() {
    return menuKey;
  }

  public void setMenuKey(String menuKey) {
    this.menuKey = menuKey;
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
	 * 获取上级菜单
	 *
	 * @return 上级菜单
	 */
	public Menu getParent() {
		return parent;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 设置上级菜单
	 *
	 * @param parent
	 *            上级菜单
	 */
	public void setParent(Menu parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级菜单
	 *
	 * @return 下级菜单
	 */
	public Set<Menu> getChildren() {
		return children;
	}

	/**
	 * 设置下级菜单
	 *
	 * @param children
	 *            下级菜单
	 */
	public void setChildren(Set<Menu> children) {
		this.children = children;
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
	 * 获取菜单跳转路径
	 * @return
	 * 		菜单跳转路径
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置菜单跳转路径
	 * @param url
	 * 		菜单跳转路径
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取菜单权限名称
	 * @return
	 * 		菜单权限名称
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * 设置菜单的权限名称
	 *
	 * @param permission
	 * 		菜单权限名称
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
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
	 * 获取所有上级菜单
	 *
	 * @return 所有上级菜单
	 */
	@Transient
	public List<Menu> getParents() {
		List<Menu> parents = new ArrayList<>();
		recursiveParents(parents, this);
		return parents;
	}

	/**
	 * 递归上级菜单
	 *
	 * @param parents
	 *            上级菜单
	 * @param menu
	 *            菜单
	 */
	private void recursiveParents(List<Menu> parents, Menu menu) {
		if (menu == null) {
			return;
		}
		Menu parent = menu.getParent();
		if (parent != null) {
			parents.add(0, parent);
			recursiveParents(parents, parent);
		}
	}

	@Transient
	@JsonView({EditView.class})
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

	public interface ListView extends BaseView {}
  public interface PermissionView extends TreeView{}
	public interface TreeView extends IdView{}

	public interface AuthRoutesView{}
}
