
package com.bootx.service.impl;

import com.bootx.dao.LogDao;
import com.bootx.entity.Log;
import com.bootx.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends BaseServiceImpl<Log, Long> implements LogService {

	@Autowired
	private LogDao logDao;

	public void clear() {
		logDao.removeAll();
	}

}
