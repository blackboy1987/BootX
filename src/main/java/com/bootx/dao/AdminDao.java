
package com.bootx.dao;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Admin;
import com.bootx.entity.Department;

import java.util.Date;

/**
 * Dao - 管理员
 * 
 * @author blackboy
 * @version 1.0
 */
public interface AdminDao extends BaseDao<Admin, Long> {

    Page<Admin> findPage(Pageable pageable, String name, String username, String email, Department department, Date beginDate, Date endDate);
}