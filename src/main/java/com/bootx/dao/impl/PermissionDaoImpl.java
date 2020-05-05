
package com.bootx.dao.impl;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.dao.PermissionDao;
import com.bootx.entity.Menu;
import com.bootx.entity.Permission;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Dao - 权限
 *
 * @author blackboy
 * @version 1.0
 */
@Repository
public class PermissionDaoImpl extends BaseDaoImpl<Permission, Long> implements PermissionDao {

	@Override
	public Boolean exists(String attributeName,String attributeValue,Long id){
		if(id==null){
			return exists(attributeName,attributeValue);
		}
		String jpql = "select permissions from Permission permissions where permissions."+attributeName+" = :attributeValue and id != :id";
		TypedQuery<Permission> query = entityManager.createQuery(jpql, Permission.class).setParameter("attributeValue", attributeValue).setParameter("id",id);
		return query.getResultList().size()>0;
	}

	@Override
	public Boolean exists(String name, Menu menu, Long id) {
		if(id==null){
			String jpql = "select permissions from Permission permissions where permissions.name = :name and permissions.menu = :menu";
			TypedQuery<Permission> query = entityManager.createQuery(jpql, Permission.class).setParameter("name", name).setParameter("menu",menu);
			return query.getResultList().size()>0;
		}
		String jpql = "select permissions from Permission permissions where permissions.name = :name and permissions.menu = :menu and id != :id";
		TypedQuery<Permission> query = entityManager.createQuery(jpql, Permission.class).setParameter("name", name).setParameter("menu",menu).setParameter("id",id);
		return query.getResultList().size()>0;
	}

  @Override
  public Page<Permission> findPage(Pageable pageable, Menu menu) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Permission> criteriaQuery = criteriaBuilder.createQuery(Permission.class);
    Root<Permission> root = criteriaQuery.from(Permission.class);
    criteriaQuery.select(root);
    Predicate restrictions = criteriaBuilder.conjunction();
    if (menu!=null) {
      restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("menu"), menu));
    }
    criteriaQuery.where(restrictions);
    return super.findPage(criteriaQuery, pageable);
  }

  @Override
  public List<Permission> findList(Integer type,Boolean isChecked,Boolean isEnabled, Menu menu) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Permission> criteriaQuery = criteriaBuilder.createQuery(Permission.class);
    Root<Permission> root = criteriaQuery.from(Permission.class);
    criteriaQuery.select(root);
    Predicate restrictions = criteriaBuilder.conjunction();
    if(type==null){
      return Collections.emptyList();
    }
    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
    if (menu!=null&&menu.getId()>0) {
      restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("menu"), menu));
    }
    if (isChecked!=null) {
      restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isChecked"), isChecked));
    }
    if (isEnabled!=null) {
      restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isEnabled"), isEnabled));
    }
    criteriaQuery.where(restrictions);
    return super.findList(criteriaQuery, null,null,null,null);
  }
}
