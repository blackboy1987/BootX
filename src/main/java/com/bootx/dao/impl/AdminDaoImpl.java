
package com.bootx.dao.impl;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.dao.AdminDao;
import com.bootx.entity.Admin;
import com.bootx.entity.Department;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * Dao - 管理员
 * 
 * @author blackboy
 * @version 1.0
 */
@Repository
public class AdminDaoImpl extends BaseDaoImpl<Admin, Long> implements AdminDao {

    public Page<Admin> findPage(Pageable pageable, String name, String username, String email, Department department, Date beginDate, Date endDate){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Admin> criteriaQuery = criteriaBuilder.createQuery(Admin.class);
        Root<Admin> root = criteriaQuery.from(Admin.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (StringUtils.isNotEmpty(name)) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("name"), "%"+name+"%"));
        }
        if (StringUtils.isNotEmpty(username)) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("username"), "%"+username+"%"));
        }
        if (StringUtils.isNotEmpty(email)) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("email"), "%"+email+"%"));
        }
        if (department!=null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("department"), department));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), endDate));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
}