
package com.bootx.service.impl;

import com.bootx.dao.MenuDao;
import com.bootx.entity.Menu;
import com.bootx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Service - 菜单
 * 
 * @author blackboy
 * @version 1.0
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {

	@Autowired
	private MenuDao menuDao;


	@Transactional(readOnly = true)
	@Override
	public List<Menu> findRoots() {
		return menuDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Menu> findRoots(Integer count) {
		return menuDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	@Override
	@Cacheable(value = "menu", condition = "#useCache")
	public List<Menu> findRoots(Integer count, boolean useCache) {
		return menuDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Menu> findParents(Menu menu, boolean recursive, Integer count) {
		return menuDao.findParents(menu, recursive, count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "menu", condition = "#useCache")
	@Override
	public List<Menu> findParents(Long menuId, boolean recursive, Integer count, boolean useCache) {
		Menu menu = menuDao.find(menuId);
		if (menuId != null && menu == null) {
			return Collections.emptyList();
		}
		return menuDao.findParents(menu, recursive, count);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Menu> findTree() {
		return menuDao.findChildren(null, true, null);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Menu> findChildren(Menu menu, boolean recursive, Integer count) {
		return menuDao.findChildren(menu, recursive, count);
	}

	@Transactional(readOnly = true)
	@Override
	@Cacheable(value = "menu", condition = "#useCache")
	public List<Menu> findChildren(Long menuId, boolean recursive, Integer count, boolean useCache) {
		Menu menu = menuDao.find(menuId);
		if (menuId != null && menu == null) {
			return Collections.emptyList();
		}
		return menuDao.findChildren(menu, recursive, count);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "menu" }, allEntries = true)
	public Menu save(Menu menu) {
		Assert.notNull(menu,"");

		setValue(menu);
		return super.save(menu);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "menu" }, allEntries = true)
	public Menu update(Menu menu) {
		Assert.notNull(menu,"");

		setValue(menu);
		for (Menu children : menuDao.findChildren(menu, true, null)) {
			setValue(children);
		}
		return super.update(menu);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "menu" }, allEntries = true)
	public Menu update(Menu menu, String... ignoreProperties) {
		return super.update(menu, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "menu" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "menu" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "menu" }, allEntries = true)
	public void delete(Menu menu) {
		super.delete(menu);
	}

	/**
	 * 设置值
	 * 
	 * @param menu
	 *            菜单
	 */
	private void setValue(Menu menu) {
		if (menu == null) {
			return;
		}
		Menu parent = menu.getParent();
		if (parent != null) {
			menu.setTreePath(parent.getTreePath() + parent.getId() + Menu.TREE_PATH_SEPARATOR);
		} else {
			menu.setTreePath(Menu.TREE_PATH_SEPARATOR);
		}
		menu.setGrade(menu.getParentIds().length);
	}

}