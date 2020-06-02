
package com.bootx.dao.impl;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.dao.PostDao;
import com.bootx.entity.Department;
import com.bootx.entity.Post;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * Dao - 角色
 *
 * @author blackboy
 * @version 1.0
 */
@Repository
public class PostDaoImpl extends BaseDaoImpl<Post, Long> implements PostDao {

    @Override
    public Page<Post> findPage(Pageable pageable, Department department, String name, String description, Integer level, Boolean isEnabled, Date beginDate, Date endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root<Post> root = criteriaQuery.from(Post.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (department!=null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("department"), department));
        }
        if (StringUtils.isNotEmpty(name)) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("name"), "%"+name+"%"));
        }
        if (StringUtils.isNotEmpty(description)) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("description"), "%"+description+"%"));
        }
        if (level!=null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("level"), level));
        }
        if (isEnabled!=null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isEnabled"), isEnabled));
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
