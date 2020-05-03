
package com.bootx.service;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Admin;
import com.bootx.entity.Department;
import com.bootx.entity.Menu;
import com.bootx.entity.User;
import com.bootx.security.AuthenticationProvider;

import java.util.Date;
import java.util.Set;

/**
 * Service - 管理员
 *
 * @author blackboy
 * @version 1.0
 */
public interface AdminService extends BaseService<Admin, Long>, AuthenticationProvider {

	/**
	 * 判断用户名是否存在
	 *
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	boolean usernameExists(String username);

	/**
	 * 根据用户名查找管理员
	 *
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 管理员，若不存在则返回null
	 */
	Admin findByUsername(String username);

	/**
	 * 判断E-mail是否存在
	 *
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	boolean emailExists(String email);

	/**
	 * 判断E-mail是否唯一
	 *
	 * @param id
	 *            ID
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否唯一
	 */
	boolean emailUnique(Long id, String email);

	/**
	 * 根据E-mail查找管理员
	 *
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 管理员，若不存在则返回null
	 */
	Admin findByEmail(String email);

	Page<Admin> findPage(Pageable pageable, String name, String username, String email, Department department, Date beginDate, Date endDate);

	String createCardNo();

	Admin getCurrent();

  Set<Menu> getMenus(User user);
}
