
package com.bootx.service.impl;

import com.bootx.dao.DepartmentDao;
import com.bootx.entity.Department;
import com.bootx.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Service - 部门
 * 
 * @author blackboy
 * @version 1.0
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Long> implements DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;


	@Transactional(readOnly = true)
	@Override
	public List<Department> findRoots() {
		return departmentDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Department> findRoots(Integer count) {
		return departmentDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	@Override
	@Cacheable(value = "department", condition = "#useCache")
	public List<Department> findRoots(Integer count, boolean useCache) {
		return departmentDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Department> findParents(Department department, boolean recursive, Integer count) {
		return departmentDao.findParents(department, recursive, count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "department", condition = "#useCache")
	@Override
	public List<Department> findParents(Long departmentId, boolean recursive, Integer count, boolean useCache) {
		Department department = departmentDao.find(departmentId);
		if (departmentId != null && department == null) {
			return Collections.emptyList();
		}
		return departmentDao.findParents(department, recursive, count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "department")
	@Override
	public List<Department> findTree() {
		return departmentDao.findChildren(null, true, null);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "department", condition = "#useCache")
	@Override
	public List<Department> findChildren(Department department, boolean recursive, Integer count) {
		return departmentDao.findChildren(department, recursive, count);
	}

	@Transactional(readOnly = true)
	@Override
	@Cacheable(value = "department", condition = "#useCache")
	public List<Department> findChildren(Long departmentId, boolean recursive, Integer count, boolean useCache) {
		Department department = departmentDao.find(departmentId);
		if (departmentId != null && department == null) {
			return Collections.emptyList();
		}
		return departmentDao.findChildren(department, recursive, count);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "department" }, allEntries = true)
	public Department save(Department department) {
		Assert.notNull(department,"");

		setValue(department);
		return super.save(department);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "department" }, allEntries = true)
	public Department update(Department department) {
		Assert.notNull(department,"");

		setValue(department);
		for (Department children : departmentDao.findChildren(department, true, null)) {
			setValue(children);
		}
		return super.update(department);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "department" }, allEntries = true)
	public Department update(Department department, String... ignoreProperties) {
		return super.update(department, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "department" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "department" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "department" }, allEntries = true)
	public void delete(Department department) {
		super.delete(department);
	}

	/**
	 * 设置值
	 * 
	 * @param department
	 *            部门
	 */
	private void setValue(Department department) {
		if (department == null) {
			return;
		}
		Department parent = department.getParent();
		if (parent != null) {
			department.setTreePath(parent.getTreePath() + parent.getId() + Department.TREE_PATH_SEPARATOR);
		} else {
			department.setTreePath(Department.TREE_PATH_SEPARATOR);
		}
		department.setGrade(department.getParentIds().length);
	}

}