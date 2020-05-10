
package com.bootx.controller.admin;

import com.bootx.entity.Admin;
import com.bootx.entity.Menu;
import com.bootx.entity.Permission;
import com.bootx.security.UserAuthenticationToken;
import com.bootx.service.*;
import com.bootx.util.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * Controller - 登陆
 *
 * @author 夏黎
 * @version 1.0
 */
@RestController("adminLoginController")
@RequestMapping("/login")
public class LoginController extends BaseController {

	@Resource
	private AdminService adminService;
	@Resource
	private RoleService roleService;

	@Resource
	private UserService userService;

  @Resource
  private PermissionService permissionService;

  @Resource
  private CaptchaService captchaService;
  @Resource
  private PostService postService;

	/**
	 * 登录页面
	 */
	@PostMapping
	public Map<String,Object> index(String type,String username, String password,String captcha, String captchaId, HttpServletRequest request) {
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
			admin.setCardNo(adminService.createCardNo());
			admin.setLockDate(null);
			admin.setIsLocked(false);
			admin.setEmail(username+"@qq.com");
			admin.setDepartment(null);
			admin.setPassword(password);
			admin.setRoles(new HashSet<>(roleService.findAll()));
			admin.setUsername(username);
			admin.setName(username);
			admin.setPost(postService.find(1L));
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

		// 验证码
		if(!captchaService.isValid(captchaId,captcha)){
			data.put("code","-2");
			data.put("status","error");
			data.put("content","验证码错误");
			return data;
		}

		data.put("code","0");
		data.put("status","ok");
		data.put("content","登陆成功");
		Map<String,Object> user = new HashMap<>();
		user.put("id",admin.getId());
		user.put("username",admin.getUsername());
		data.put("user",user);
		userService.login(new UserAuthenticationToken(Admin.class, username, password, false, request.getRemoteAddr()));
		// 登陆成功之后，把相关信息加密到token里面
		data.put("currentAuthority",authButtons());
		Map<String,Object> tokenMap = new HashMap<>();
		tokenMap.put("id",admin.getId());
		tokenMap.put("username",admin.getUsername());
		data.put("token", JWTUtils.create(admin.getId()+"",tokenMap));
		return data;
	}

  private Set<String> authButtons() {
    Set<String> buttons = new HashSet<>();
    // 获取不需要校验的
    Menu root = new Menu();
    root.setId(-1L);
    List<Permission> permissions = permissionService.findList(0,false,true,root);
    for (Permission permission:permissions) {
      buttons.addAll(permission.getUrls());
    }
    Admin admin = adminService.getCurrent();
    if(admin!=null){
      Set<Menu> menus = adminService.getMenus(admin);
      for (Menu menu:menus) {
        permissions = permissionService.findList(0,true,true,menu);
        for (Permission permission:permissions) {
          buttons.addAll(permission.getUrls());
        }
      }
    }
    return buttons;
  }
}
