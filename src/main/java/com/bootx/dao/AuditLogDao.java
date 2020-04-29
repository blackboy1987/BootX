
package com.bootx.dao;

import com.bootx.entity.AuditLog;

/**
 * Dao - 审计日志
 * 
 * @author blackboy
 * @version 1.0
 */
public interface AuditLogDao extends BaseDao<AuditLog, Long> {

	/**
	 * 删除所有
	 */
	void removeAll();

}