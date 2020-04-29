
package com.bootx.dao.impl;

import com.bootx.dao.DepartmentDao;
import com.bootx.entity.Department;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Dao - 部门
 * 
 * @author blackboy
 * @version 1.0
 */
@Repository
public class DepartmentDaoImpl extends BaseDaoImpl<Department, Long> implements DepartmentDao {


	@Override
	public List<Department> findRoots(Integer count) {
		String jpql = "select department from Department department where department.parent is null order by department.order asc";
		TypedQuery<Department> query = entityManager.createQuery(jpql, Department.class);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Override
	public List<Department> findParents(Department department, boolean recursive, Integer count) {
		if (department == null || department.getParent() == null) {
			return Collections.emptyList();
		}
		TypedQuery<Department> query;
		if (recursive) {
			String jpql = "select department from Department department where department.id in (:ids) order by department.grade asc";
			query = entityManager.createQuery(jpql, Department.class).setParameter("ids", Arrays.asList(department.getParentIds()));
		} else {
			String jpql = "select department from Department department where department = :department";
			query = entityManager.createQuery(jpql, Department.class).setParameter("department", department.getParent());
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Override
	public List<Department> findChildren(Department department, boolean recursive, Integer count) {
		TypedQuery<Department> query;
		if (recursive) {
			if (department != null) {
				String jpql = "select department from Department department where department.treePath like :treePath order by department.grade asc, department.order asc";
				query = entityManager.createQuery(jpql, Department.class).setParameter("treePath", "%" + Department.TREE_PATH_SEPARATOR + department.getId() + Department.TREE_PATH_SEPARATOR + "%");
			} else {
				String jpql = "select department from Department department order by department.grade asc, department.order asc";
				query = entityManager.createQuery(jpql, Department.class);
			}
			if (count != null) {
				query.setMaxResults(count);
			}
			List<Department> result = query.getResultList();
			sort(result);
			return result;
		} else {
			String jpql = "select department from Department department where department.parent = :parent order by department.order asc";
			query = entityManager.createQuery(jpql, Department.class).setParameter("parent", department);
			if (count != null) {
				query.setMaxResults(count);
			}
			return query.getResultList();
		}
	}

	/**
	 * 排序部门
	 * 
	 * @param departments
	 *            部门
	 */
	private void sort(List<Department> departments) {
		if (CollectionUtils.isEmpty(departments)) {
			return;
		}
		final Map<Long, Integer> orderMap = new HashMap<>();
		for (Department department : departments) {
			orderMap.put(department.getId(), department.getOrder());
		}
		Collections.sort(departments, new Comparator<Department>() {
			@Override
			public int compare(Department department1, Department department2) {
				Long[] ids1 = (Long[]) ArrayUtils.add(department1.getParentIds(), department1.getId());
				Long[] ids2 = (Long[]) ArrayUtils.add(department2.getParentIds(), department2.getId());
				Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
				Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
				CompareToBuilder compareToBuilder = new CompareToBuilder();
				while (iterator1.hasNext() && iterator2.hasNext()) {
					Long id1 = iterator1.next();
					Long id2 = iterator2.next();
					Integer order1 = orderMap.get(id1);
					Integer order2 = orderMap.get(id2);
					compareToBuilder.append(order1, order2).append(id1, id2);
					if (!iterator1.hasNext() || !iterator2.hasNext()) {
						compareToBuilder.append(department1.getGrade(), department2.getGrade());
					}
				}
				return compareToBuilder.toComparison();
			}
		});
	}

}