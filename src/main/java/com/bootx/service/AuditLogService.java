
package com.bootx.service;

import com.bootx.entity.AuditLog;

/**
 * Service - 审计日志
 * 
 * @author blackboy
 * @version 1.0
 */
public interface AuditLogService extends BaseService<AuditLog, Long> {

	/**
	 * 创建审计日志(异步)
	 * 
	 * @param auditLog
	 *            审计日志
	 */
	void create(AuditLog auditLog);

	/**
	 * 清空审计日志
	 */
	void clear();

}