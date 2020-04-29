
package com.bootx.dao.impl;

import com.bootx.dao.AuditLogDao;
import com.bootx.entity.AuditLog;
import org.springframework.stereotype.Repository;

/**
 * Dao - 审计日志
 * 
 * @author blackboy
 * @version 1.0
 */
@Repository
public class AuditLogDaoImpl extends BaseDaoImpl<AuditLog, Long> implements AuditLogDao {

	@Override
	public void removeAll() {
		String jpql = "delete from AuditLog auditLog";
		entityManager.createQuery(jpql).executeUpdate();
	}

}