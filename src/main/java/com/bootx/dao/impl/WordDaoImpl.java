
package com.bootx.dao.impl;

import com.bootx.dao.WordDao;
import com.bootx.entity.Word;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Dao - 角色
 *
 * @author blackboy
 * @version 1.0
 */
@Repository
public class WordDaoImpl extends BaseDaoImpl<Word, Long> implements WordDao {


  public void clearDefault(Word exclude) {
    Assert.notNull(exclude,"");

    String jpql = "update Word word set word.isDefault = false where word.isDefault = true and word != :exclude";
    entityManager.createQuery(jpql).setParameter("exclude", exclude).executeUpdate();
  }

}
