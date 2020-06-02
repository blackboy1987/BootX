
package com.bootx.controller.admin;

import com.bootx.common.Message;
import com.bootx.common.Setting;
import com.bootx.entity.Admin;
import com.bootx.entity.BaseEntity;
import com.bootx.service.AdminService;
import com.bootx.service.DepartmentService;
import com.bootx.service.PostService;
import com.bootx.service.RoleService;
import com.bootx.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;

/**
 * Controller - 管理员
 *
 * @author blackboy
 * @version 1.0
 */
@RestController
@RequestMapping("/register")
public class RegisterController extends BaseController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DepartmentService departmentService;
	@Resource
	private PostService postService;


	/**
	 * 保存
	 */
	@PostMapping
	public Message save(Admin admin) {
	  Setting setting = SystemUtils.getSetting();
	  admin.setPost(postService.find(1L));
		admin.setRoles(new HashSet<>(roleService.findAll()));
		admin.setDepartment(departmentService.find(1L));
		if(StringUtils.isEmpty(admin.getCardNo())){
		  admin.setCardNo(adminService.createCardNo());
    }
    if(admin.getIsEnabled()==null){
      admin.setIsEnabled(true);
    }
    admin.setEmail(admin.getUsername()+"@qq.com");
    admin.setPassword(setting.getDefaultPassword());

    Map<String,String> validResults = isValid1(admin, BaseEntity.Save.class);
    if(!validResults.isEmpty()){
      return Message.error("参数错误",validResults);
    }

    if (adminService.usernameExists(admin.getUsername())) {
      return Message.error("用户名已存在");
    }
    if (adminService.emailExists(admin.getEmail())) {
      return Message.error("邮箱已存在");
    }
    admin.setIsLocked(false);
    admin.setLockDate(null);
    admin.setLastLoginIp(null);
    admin.setLastLoginDate(null);
    adminService.save(admin);
    return Message.success("注册成功");

	}
}
