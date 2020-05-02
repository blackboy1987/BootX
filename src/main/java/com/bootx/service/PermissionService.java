
package com.bootx.service;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Menu;
import com.bootx.entity.Permission;

/**
 * Service - 权限
 *
 * @author blackboy
 * @version 1.0
 */
public interface PermissionService extends BaseService<Permission, Long> {

	Boolean exists(Permission permissions);

	Page<Permission> findPage(Pageable pageable, Menu menu);
}
