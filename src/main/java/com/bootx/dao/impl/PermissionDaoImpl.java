
package com.bootx.dao.impl;

import com.bootx.dao.PermissionDao;
import com.bootx.entity.Menu;
import com.bootx.entity.Permission;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

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
}