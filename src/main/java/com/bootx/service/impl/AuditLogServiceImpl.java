
package com.bootx.service.impl;

import com.bootx.dao.AuditLogDao;
import com.bootx.entity.AuditLog;
import com.bootx.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service - 审计日志
 * 
 * @author blackboy
 * @version 1.0
 */
@Service
public class AuditLogServiceImpl extends BaseServiceImpl<AuditLog, Long> implements AuditLogService {

	@Autowired
	private AuditLogDao auditLogDao;

	@Async
	@Override
	public void create(AuditLog auditLog) {
		auditLogDao.persist(auditLog);
	}

	@Override
	public void clear() {
		auditLogDao.removeAll();
	}

}