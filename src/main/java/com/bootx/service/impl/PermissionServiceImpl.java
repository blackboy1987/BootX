
package com.bootx.service.impl;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.dao.PermissionDao;
import com.bootx.entity.Menu;
import com.bootx.entity.Permission;
import com.bootx.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission, Long> implements PermissionService {

	@Autowired
	private PermissionDao permissionsDao;

	@Override
	@Transactional
	@CacheEvict(value = { "permission","authorization" }, allEntries = true)
	public Permission save(Permission permissions) {
		Assert.notNull(permissions,"");
		return super.save(permissions);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "permission","authorization" }, allEntries = true)
	public Permission update(Permission permission) {
		Assert.notNull(permission,"");
		return super.update(permission);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "permission","authorization" }, allEntries = true)
	public Permission update(Permission permission, String... ignoreProperties) {
		return super.update(permission, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "permission","authorization" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "permission","authorization" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "permission","authorization" }, allEntries = true)
	public void delete(Permission permission) {
		super.delete(permission);
	}


	@Override
	public Boolean exists(Permission permission){
		return permissionsDao.exists(permission.getName(),permission.getMenu(),permission.getId());
	}

  @Override
  public Page<Permission> findPage(Pageable pageable, Menu menu) {
    return permissionsDao.findPage(pageable,menu);
  }
}
