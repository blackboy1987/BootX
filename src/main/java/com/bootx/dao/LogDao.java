
package com.bootx.dao;

import com.bootx.entity.Log;

public interface LogDao extends BaseDao<Log, Long> {

	void removeAll();

}
