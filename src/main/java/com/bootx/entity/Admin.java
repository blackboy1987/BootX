
package com.bootx.entity;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Entity - 管理员
 *
 * @author blackboy
 * @version 1.0
 */
@Entity
@Table(name = "bootx_admin")
public class Admin extends User {

	private static final long serialVersionUID = -4000007477538426L;

	/**
	 * 用户名
	 */
	@NotEmpty(groups = Save.class)
	@Length(min = 4, max = 20)
	@Pattern.List({ @Pattern(regexp = "^[0-9a-zA-Z_\\u4e00-\\u9fa5]+$"), @Pattern(regexp = "^.*[^\\d].*$") })
	@Column(nullable = false, updatable = false, unique = true)
	@JsonView({ListView.class,EditView.class})
	private String username;

  @NotEmpty(groups = Save.class)
  @Column(nullable = false, updatable = false, unique = true)
  @JsonView({ListView.class,EditView.class})
	private String cardNo;

	/**
	 * 密码
	 */
	@NotEmpty(groups = Save.class)
	@Length(min = 4, max = 20)
	@Transient
	private String password;

	/**
	 * 加密密码
	 */
	@Column(nullable = false)
	private String encodedPassword;

	/**
	 * E-mail
	 */
	@NotEmpty
	@Email
	@Length(max = 200)
	@Column(nullable = false, unique = true)
	@JsonView({ListView.class,EditView.class})
	private String email;

	/**
	 * 姓名
	 */
	@Length(max = 200)
	@JsonView({ListView.class,EditView.class})
	private String name;

  /**
   * 姓名
   */
  @Length(max = 200)
  @JsonView({ListView.class,EditView.class})
  private String mobile;

	/**
	 * 部门
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Department department;

	/**
	 * 角色
	 */
	@NotEmpty
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Role> roles = new HashSet<>();

	@NotNull
  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
	private Post post;

	/**
	 * 获取用户名
	 *
	 * @return 用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户名
	 *
	 * @param username
	 *            用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getCardNo() {
    return cardNo;
  }

  public void setCardNo(String cardNo) {
    this.cardNo = cardNo;
  }

  /**
	 * 获取密码
	 *
	 * @return 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码
	 *
	 * @param password
	 *            密码
	 */
	public void setPassword(String password) {
		this.password = password;
		if (password != null) {
			setEncodedPassword(DigestUtils.md5Hex(password));
		}
	}

	/**
	 * 获取加密密码
	 *
	 * @return 加密密码
	 */
	public String getEncodedPassword() {
		return encodedPassword;
	}

	/**
	 * 设置加密密码
	 *
	 * @param encodedPassword
	 *            加密密码
	 */
	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	/**
	 * 获取E-mail
	 *
	 * @return E-mail
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置E-mail
	 *
	 * @param email
	 *            E-mail
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取姓名
	 *
	 * @return 姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置姓名
	 *
	 * @param name
	 *            姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  /**
	 * 获取角色
	 *
	 * @return 角色
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * 设置角色
	 *
	 * @param roles
	 *            角色
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	@Transient
	public String getDisplayName() {
		return getUsername();
	}

	@Override
	@Transient
	public Object getPrincipal() {
		return getUsername();
	}

	@Override
	@Transient
	public Object getCredentials() {
		return getPassword();
	}

	@Override
	@Transient
	public boolean isValidCredentials(Object credentials) {
		return credentials != null && DigestUtils.md5Hex(credentials instanceof char[] ? new String((char[]) credentials) : credentials.toString()).equals(getEncodedPassword());
	}

	@Transient
	@JsonView({ListView.class,EditView.class})
	public List<Long> getRoleIds() {
		List<Long> roleIds = new ArrayList<>();
		for (Role role:roles) {
			roleIds.add(role.getId());
		}
		return roleIds;
	}

	@Transient
	@JsonView({ListView.class,EditView.class})
	public List<String> getRoleNames() {
		List<String> roleNames = new ArrayList<>();
		for (Role role:roles) {
			roleNames.add(role.getName());
		}
		return roleNames;
	}


	@Transient
	@JsonView({ListView.class,EditView.class})
	public Long getDepartmentId() {
		if(department!=null){
			return department.getId();
		}
		return null;
	}

	@Transient
	@JsonView({ListView.class,EditView.class})
	public String getDepartmentName() {
		if(department!=null){
			return department.getName();
		}
		return null;
	}

  @Transient
  @JsonView({EditView.class})
  public Long getPostId() {
    if(post!=null){
      return post.getId();
    }
    return null;
  }

  @Transient
  @JsonView({ListView.class})
  public String getPostName() {
    if(post!=null){
      return post.getName();
    }
    return null;
  }

	/**
	 * 持久化前处理
	 */
	@PrePersist
	public void prePersist() {
		setUsername(StringUtils.lowerCase(getUsername()));
		setEmail(StringUtils.lowerCase(getEmail()));
	}

	/**
	 * 更新前处理
	 */
	@PreUpdate
	public void preUpdate() {
		setEmail(StringUtils.lowerCase(getEmail()));
	}

	public interface ListView extends BaseView{

	}

	public interface EditView extends IdView{}
}
