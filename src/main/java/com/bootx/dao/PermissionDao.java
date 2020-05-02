
package com.bootx.dao;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Menu;
import com.bootx.entity.Permission;

/**
 * Dao - 权限
 *
 * @author blackboy
 * @version 1.0
 */
public interface PermissionDao extends BaseDao<Permission, Long> {

	Boolean exists(String attributeName, String attributeValue, Long id);


	Boolean exists(String name, Menu menu, Long id);

  Page<Permission> findPage(Pageable pageable, Menu menu);

}
