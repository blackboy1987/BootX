
package com.bootx.service;

import com.bootx.entity.Department;

import java.util.List;

/**
 * Service - 部门
 * 
 * @author blackboy
 * @version 1.0
 */
public interface DepartmentService extends BaseService<Department, Long> {

	/**
	 * 查找顶级部门
	 * 
	 * @return 顶级部门
	 */
	List<Department> findRoots();

	/**
	 * 查找顶级部门
	 * 
	 * @param count
	 *            数量
	 * @return 顶级部门
	 */
	List<Department> findRoots(Integer count);

	/**
	 * 查找顶级部门
	 * 
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 顶级部门
	 */
	List<Department> findRoots(Integer count, boolean useCache);

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
	 * 查找上级部门
	 * 
	 * @param departmentId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 上级部门
	 */
	List<Department> findParents(Long departmentId, boolean recursive, Integer count, boolean useCache);

	/**
	 * 查找部门树
	 * 
	 * @return 部门树
	 */
	List<Department> findTree();

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

	/**
	 * 查找下级部门
	 * 
	 * @param departmentId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 下级部门
	 */
	List<Department> findChildren(Long departmentId, boolean recursive, Integer count, boolean useCache);

}