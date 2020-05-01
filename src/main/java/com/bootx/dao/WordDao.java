
package com.bootx.dao;

import com.bootx.entity.Word;

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
}
