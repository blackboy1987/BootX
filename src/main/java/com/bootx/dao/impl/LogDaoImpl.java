
package com.bootx.dao.impl;

import com.bootx.dao.LogDao;
import com.bootx.entity.Log;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

@Repository
public class LogDaoImpl extends BaseDaoImpl<Log, Long> implements LogDao {

	public void removeAll() {
		String jpql = "delete from Log log";
		entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).executeUpdate();
	}

}
