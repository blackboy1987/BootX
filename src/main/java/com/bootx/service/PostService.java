
package com.bootx.service;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Department;
import com.bootx.entity.Post;

import java.util.Date;

/**
 * Service - 角色
 *
 * @author blackboy
 * @version 1.0
 */
public interface PostService extends BaseService<Post, Long> {
    Page<Post> findPage(Pageable pageable, Department department, String name, String description, Integer level, Boolean isEnabled, Date beginDate, Date endDate);
}
