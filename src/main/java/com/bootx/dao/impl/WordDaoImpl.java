
package com.bootx.dao.impl;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.dao.WordDao;
import com.bootx.entity.Word;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

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
public class WordDaoImpl extends BaseDaoImpl<Word, Long> implements WordDao {


  public void clearDefault(Word exclude) {
    Assert.notNull(exclude,"");

    String jpql = "update Word word set word.isDefault = false where word.isDefault = true and word != :exclude";
    entityManager.createQuery(jpql).setParameter("exclude", exclude).executeUpdate();
  }

  @Override
  public Page<Word> findPage(Pageable pageable, String name, Boolean isEnabled, Date beginDate, Date endDate) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Word> criteriaQuery = criteriaBuilder.createQuery(Word.class);
    Root<Word> root = criteriaQuery.from(Word.class);
    criteriaQuery.select(root);
    Predicate restrictions = criteriaBuilder.conjunction();
    if (StringUtils.isNotEmpty(name)) {
      restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("name"), "%"+name+"%"));
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
