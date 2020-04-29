
package com.bootx.controller.admin;

import com.bootx.entity.Admin;
import com.bootx.security.UserAuthenticationToken;
import com.bootx.service.AdminService;
import com.bootx.service.RoleService;
import com.bootx.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/**
 * Controller - 登陆
 * 
 * @author 夏黎
 * @version 1.0
 */
@RestController("adminLoginController")
@RequestMapping("//admin/api/login")
public class LoginController extends BaseController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	/**
	 * 登录页面
	 */
	@PostMapping
	public Map<String,Object> index(String type,String username, String password, HttpServletRequest request) {
		Map<String,Object> data = new HashMap<>();
		data.put("type",type);
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
			data.put("code","-1");
			data.put("content","请输入用户名或密码");
			return data;
		}
		Admin admin = adminService.findByUsername(username);
		if(admin==null){
			admin = new Admin();
			admin.setLockDate(null);
			admin.setIsLocked(false);
			admin.setEmail(username+"@qq.com");
			admin.setDepartment(null);
			admin.setPassword(password);
			admin.setRoles(new HashSet<>(roleService.findAll()));
			admin.setUsername(username);
			admin.setName(username);
			admin.setLastLoginDate(new Date());
			admin.setLastLoginIp(request.getRemoteAddr());
			admin.setIsEnabled(true);
			adminService.save(admin);
		}



		if(admin==null){
			data.put("code","-1");
			data.put("status","error");
			data.put("content","用户名或密码输入错误");
			return data;
		}
		if(!admin.isValidCredentials(password)){
			data.put("code","-1");
			data.put("status","error");
			data.put("content","用户名或密码输入错误");
			return data;
		}
		data.put("code","0");
		data.put("status","ok");
		data.put("content","登陆成功");
		Map<String,Object> user = new HashMap<>();
		user.put("id",admin.getId());
		user.put("username",admin.getUsername());
		data.put("user",user);
		data.put("currentAuthority","admin");
		userService.login(new UserAuthenticationToken(Admin.class, username, password, false, request.getRemoteAddr()));
		return data;
	}
}