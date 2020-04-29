
package com.bootx.event;

import com.bootx.entity.User;

/**
 * Event - 用户注销
 * 
 * @author IGOMALL  Team
 * @version 1.0
 */
public class UserLoggedOutEvent extends UserEvent {

	private static final long serialVersionUID = 8560275705072178478L;

	/**
	 * 构造方法
	 * 
	 * @param source
	 *            事件源
	 * @param user
	 *            用户
	 */
	public UserLoggedOutEvent(Object source, User user) {
		super(source, user);
	}

}