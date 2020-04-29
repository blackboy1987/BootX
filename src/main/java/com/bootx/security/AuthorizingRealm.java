
package com.bootx.security;

import com.bootx.entity.User;
import com.bootx.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Security - 授权域
 * 
 * @author IGOMALL  Team
 * @version 1.0
 */
public class AuthorizingRealm extends org.apache.shiro.realm.AuthorizingRealm {

	@Autowired
	private UserService userService;

	/**
	 * 获取认证信息
	 * 
	 * @param authenticationToken
	 *            令牌
	 * @return 认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
		User user = userService.getUser(authenticationToken);
		return new SimpleAuthenticationInfo(user, authenticationToken.getCredentials(), getName());
	}

	/**
	 * 获取授权信息
	 * 
	 * @param principalCollection
	 *            身份集合
	 * @return 授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		for (Object principal : principalCollection.fromRealm(getName())) {
			if (principal != null && principal instanceof User) {
				User user = (User) principal;
				Set<String> permissions = userService.getPermissions(user);
				if (permissions != null) {
					authorizationInfo.addStringPermissions(permissions);
				}
			}
		}
		return authorizationInfo;
	}

	@Override
	public boolean supports(AuthenticationToken authenticationToken) {
		return authenticationToken != null && (authenticationToken instanceof UserAuthenticationToken);
	}

}