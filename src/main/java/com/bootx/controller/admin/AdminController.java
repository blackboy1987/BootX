
package com.bootx.controller.admin;

import com.bootx.common.Setting;
import com.bootx.util.SystemUtils;
import com.fasterxml.jackson.annotation.JsonView;
import com.bootx.common.Message;
import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Admin;
import com.bootx.entity.BaseEntity;
import com.bootx.service.AdminService;
import com.bootx.service.DepartmentService;
import com.bootx.service.RoleService;
import com.bootx.service.UserService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Controller - 管理员
 *
 * @author blackboy
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController extends BaseController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DepartmentService departmentService;

  /**
   * 添加
   */
  @PostMapping("/add")
  public Map<String,Object> add() {
    Map<String,Object> data = new HashMap<>();
    data.put("cardNo",adminService.createCardNo());
    return data;
  }


	/**
	 * 检查用户名是否存在
	 */
	@GetMapping("/check_username")
	public @ResponseBody boolean checkUsername(String username) {
		return StringUtils.isNotEmpty(username) && !adminService.usernameExists(username);
	}

	/**
	 * 检查E-mail是否唯一
	 */
	@GetMapping("/check_email")
	public @ResponseBody boolean checkEmail(Long id, String email) {
		return StringUtils.isNotEmpty(email) && adminService.emailUnique(id, email);
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public Message save(Admin admin, Long[] roleIds,Long departmentId) {
		admin.setRoles(new HashSet<>(roleService.findList(roleIds)));
		admin.setDepartment(departmentService.find(departmentId));
		if(StringUtils.isEmpty(admin.getCardNo())){
		  admin.setCardNo(adminService.createCardNo());
    }
    if(admin.getIsEnabled()==null){
      admin.setIsEnabled(true);
    }
    admin.setEmail(admin.getUsername()+"@qq.com");
    admin.setPassword("123456");

    Map<String,String> validResults = isValid1(admin, BaseEntity.Save.class);
    if(!validResults.isEmpty()){
      return Message.error1("参数错误",validResults);
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
    return Message.success("添加成功");

	}

	/**
	 * 编辑
	 */
	@PostMapping("/edit")
	@JsonView(Admin.EditView.class)
	public Admin edit(Long id) {
		return adminService.find(id);
	}

  /**
   * 更新
   */
  @PostMapping("/update")
  public Message update(Admin admin, Long[] roleIds,Long departmentId, Boolean isLocked) {
    admin.setDepartment(departmentService.find(departmentId));
    if(admin.getIsEnabled()==null){
      admin.setIsEnabled(false);
    }
    if(admin.getIsLocked()==null){
      admin.setIsLocked(false);
      isLocked=true;
    }
    if(admin.getIsLocked()){
      admin.setLockDate(new Date());
      isLocked=false;
    }

    admin.setEmail(admin.getUsername()+"@qq.com");
    admin.setRoles(new HashSet<>(roleService.findList(roleIds)));
    Map<String,String> validResults = isValid1(admin);
    if(!validResults.isEmpty()){
      return Message.error1("参数错误",validResults);
    }
    if (!adminService.emailUnique(admin.getId(), admin.getEmail())) {
      return Message.error("邮箱已存在");
    }
    Admin pAdmin = adminService.find(admin.getId());
    if (pAdmin == null) {
      return Message.error("对象为空");
    }
    if (BooleanUtils.isTrue(pAdmin.getIsLocked()) && BooleanUtils.isTrue(isLocked)) {
      userService.unlock(admin);
      adminService.update(admin, "username", "encodedPassword", "lastLoginIp", "lastLoginDate");
    } else {
      // adminService.update(admin, "username", "encodedPassword", "isLocked", "lockDate", "lastLoginIp", "lastLoginDate");
      adminService.update(admin, "username", "encodedPassword", "lastLoginIp", "lastLoginDate");
    }
    return SUCCESS_MESSAGE;
  }


	/**
	 * 列表
	 */
	@PostMapping("/list")
	@JsonView(Admin.ListView.class)
	public Page<Admin> list(Pageable pageable,String name,String username,String email,Long departmentId,Date beginDate,Date endDate) {
		return adminService.findPage(pageable,name,username,email,departmentService.find(departmentId),beginDate,endDate);
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public Message delete(Long[] ids) {
		if (ids.length >= adminService.count()) {
			return Message.error("参数错误");
		}
		adminService.delete(ids);
		return Message.success("删除成功");
	}

	/**
	 * 禁用
	 */
	@PostMapping("/disabled")
	public Message disabled(Long id) {
		Admin admin = adminService.find(id);
		if(admin==null){
			return Message.error("账号不存在");
		}
		admin.setIsEnabled(false);
		adminService.update(admin);
		return Message.success("操作成功");
	}

	/**
	 * 启用
	 */
	@PostMapping("/enabled")
	public Message enabled(Long id) {
		Admin admin = adminService.find(id);
		if(admin==null){
			return Message.error("账号不存在");
		}
		admin.setIsEnabled(true);
		adminService.update(admin);
		return Message.success("操作成功");
	}

	/**
	 * 重置密码
	 */
	@PostMapping("/reset")
	public Message reset(Long id) {
		Setting setting = SystemUtils.getSetting();
		Admin admin = adminService.find(id);
		if(admin==null){
			return Message.error("账号不存在");
		}
		admin.setPassword(setting.getDefaultPassword());
		adminService.update(admin);
		return Message.success("操作成功");
	}

}
