
package com.bootx.service;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Role;

import java.util.Date;

/**
 * Service - 角色
 *
 * @author blackboy
 * @version 1.0
 */
public interface RoleService extends BaseService<Role, Long> {
    Page<Role> findPage(Pageable pageable, String name, Boolean isEnabled, Date beginDate, Date endDate);

}
