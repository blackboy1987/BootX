
package com.bootx.service;

import com.bootx.entity.Permission;

/**
 * Service - 权限
 * 
 * @author blackboy
 * @version 1.0
 */
public interface PermissionService extends BaseService<Permission, Long> {

	Boolean exists(Permission permissions);
}