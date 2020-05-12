
package com.bootx.service.impl;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.common.Setting;
import com.bootx.dao.AdminDao;
import com.bootx.entity.*;
import com.bootx.service.AdminService;
import com.bootx.util.JWTUtils;
import com.bootx.util.SystemUtils;
import com.bootx.util.WebUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Service - 管理员
 *
 * @author blackboy
 * @version 1.0
 */
@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long> implements AdminService {

	@Autowired
	private AdminDao adminDao;

  @Resource
  private RedisTemplate<String,String> redisTemplate;

	@Override
	@Transactional(readOnly = true)
	public Admin getUser(Object principal) {
		Assert.notNull(principal,"");
		Assert.isInstanceOf(String.class, principal);

		return findByUsername((String) principal);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getPermissions(User user) {
		Assert.notNull(user,"");
		Assert.isInstanceOf(Admin.class, user);

		Admin admin = adminDao.find(user.getId());
		Set<String> result = new HashSet<>();
		if (admin != null && admin.getRoles() != null) {
			for (Role role : admin.getRoles()) {
				if (role.getPermissions() != null) {
					result.addAll(role.getPermissionUrls());
				}
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean supports(Class<?> userClass) {
		return userClass != null && Admin.class.isAssignableFrom(userClass);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return adminDao.exists("username", StringUtils.lowerCase(username));
	}

	@Override
	@Transactional(readOnly = true)
	public Admin findByUsername(String username) {
		return adminDao.find("username", StringUtils.lowerCase(username));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return adminDao.exists("email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean emailUnique(Long id, String email) {
		return adminDao.unique(id, "email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public Admin findByEmail(String email) {
		return adminDao.find("email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin save(Admin admin) {
		return super.save(admin);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin update(Admin admin) {
		return super.update(admin);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin update(Admin admin, String... ignoreProperties) {
		return super.update(admin, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Admin admin) {
		super.delete(admin);
	}

	@Override
	public Page<Admin> findPage(Pageable pageable, String name, String username, String email, Department department, Date beginDate, Date endDate) {
		return adminDao.findPage(pageable,name,username,email,department,beginDate,endDate);
	}

	@Override
	public String createCardNo(){
	  Long max = jdbcTemplate.queryForObject("select max(CAST(card_no AS SIGNED )) from bootx_admin",Long.class);
	  if(max==null){
	    max = 0L;
    }
	  return createCardNo(max+1);
  }

  private String createCardNo(Long value){
    Setting setting = SystemUtils.getSetting();
    Integer codeNoLength = setting.getCodeNoLength();
    Integer valueLength = String.valueOf(value).length();
    StringBuilder result = new StringBuilder(value + "");
    if(codeNoLength>valueLength){
      for (int i=0;i<codeNoLength-valueLength;i++){
        result.insert(0, "0");
      }
    }
    return result.toString();
  }

  @Override
  public Admin getCurrent() {
    HttpServletRequest request = WebUtils.getRequest();
    try {
      String authorizationToken = request.getHeader("Authorization");
      Claims claims = JWTUtils.parseToken(authorizationToken);
      String token = redisTemplate.opsForValue().get(claims.getId());
      if(StringUtils.equals(token,authorizationToken)){
        return find(Long.valueOf(claims.getId()));
      }
      return null;
    }catch (Exception e){
      return null;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Set<Menu> getMenus(User user) {
    Assert.notNull(user,"");
    Assert.isInstanceOf(Admin.class, user);

    Admin admin = adminDao.find(user.getId());
    Set<Menu> result = new HashSet<>();
    if (admin != null && admin.getRoles() != null) {
      for (Role role : admin.getRoles()) {
        if (role.getMenus() != null) {
          result.addAll(role.getMenus());
        }
      }
    }
    return result;
  }
}
