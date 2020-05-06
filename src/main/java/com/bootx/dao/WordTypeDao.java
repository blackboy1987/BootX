
package com.bootx.dao;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.WordType;

import java.util.Date;

/**
 * Dao - 角色
 *
 * @author blackboy
 * @version 1.0
 */
public interface WordTypeDao extends BaseDao<WordType, Long> {
    Page<WordType> findPage(Pageable pageable, String name,Boolean isEnabled, Date beginDate, Date endDate);
}
