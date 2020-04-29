
package com.bootx.dao.impl;

import com.bootx.dao.UserDao;
import com.bootx.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Dao - 用户
 * 
 * @author blackboy
 * @version 1.0
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Long> implements UserDao {

}