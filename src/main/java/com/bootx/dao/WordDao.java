
package com.bootx.dao;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Word;

import java.util.Date;

/**
 * Dao - 角色
 *
 * @author blackboy
 * @version 1.0
 */
public interface WordDao extends BaseDao<Word, Long> {


  /**
   * 清除默认
   *
   * @param exclude
   *            排除会员等级
   */
  void clearDefault(Word exclude);

  Page<Word> findPage(Pageable pageable, String name, Boolean isEnabled, Date beginDate, Date endDate);
}
