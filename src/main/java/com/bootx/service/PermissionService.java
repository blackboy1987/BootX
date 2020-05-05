
package com.bootx.service;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Menu;
import com.bootx.entity.Permission;

import java.util.List;

/**
 * Service - 权限
 *
 * @author blackboy
 * @version 1.0
 */
public interface PermissionService extends BaseService<Permission, Long> {

	Boolean exists(Permission permissions);

	Page<Permission> findPage(Pageable pageable, Menu menu);

  List<Permission> findList(Integer type,Boolean isChecked,Boolean isEnabled,Menu menu);
}
