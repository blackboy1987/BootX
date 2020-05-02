
package com.bootx.service;

import com.bootx.entity.Log;

public interface LogService extends BaseService<Log, Long> {

	void clear();

}
