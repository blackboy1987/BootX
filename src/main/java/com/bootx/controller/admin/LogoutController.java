
package com.bootx.controller.admin;

import com.bootx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Controller - 登陆
 *
 * @author 夏黎
 * @version 1.0
 */
@RestController("adminLogoutController")
@RequestMapping("/api/logout")
public class LogoutController extends BaseController {

	@Autowired
	private UserService userService;


	/**
	 * 登录页面
	 */
	@PostMapping
	public Map<String,Object> index() {
		Map<String,Object> data = new HashMap<>();
    userService.logout();
		return data;
	}
}
