
package com.bootx.dao;

import com.bootx.entity.Department;

import java.util.List;

/**
 * Dao - 部门
 * 
 * @author blackboy
 * @version 1.0
 */
public interface DepartmentDao extends BaseDao<Department, Long> {


	/**
	 * 查找顶级部门
	 * 
	 * @param count
	 *            数量
	 * @return 顶级部门
	 */
	List<Department> findRoots(Integer count);

	/**
	 * 查找上级部门
	 * 
	 * @param department
	 *            部门
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级部门
	 */
	List<Department> findParents(Department department, boolean recursive, Integer count);

	/**
	 * 查找下级部门
	 * 
	 * @param department
	 *            部门
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级部门
	 */
	List<Department> findChildren(Department department, boolean recursive, Integer count);

}