
package com.bootx.entity;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity - 角色
 *
 * @author blackboy
 * @version 1.0
 */
@Entity
@Table(name = "bootx_post")
public class Post extends OrderedEntity<Long> {

	private static final long serialVersionUID = -6614052029623997372L;

	/**
	 * 名称
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	@JsonView({TreeView.class,ListView.class,EditView.class})
	private String name;

	@NotNull
  @Column(nullable = false)
  @JsonView({ListView.class,EditView.class})
	private Integer level;

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
	 * 管理员
	 */
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private Set<Admin> admins = new HashSet<>();

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

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
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

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

  public Set<Admin> getAdmins() {
    return admins;
  }

  public void setAdmins(Set<Admin> admins) {
    this.admins = admins;
  }
}
