
package com.bootx.service;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Menu;

import java.util.List;

/**
 * Service - 菜单
 *
 * @author blackboy
 * @version 1.0
 */
public interface MenuService extends BaseService<Menu, Long> {

	/**
	 * 查找顶级菜单
	 *
	 * @return 顶级菜单
	 */
	List<Menu> findRoots();

	/**
	 * 查找顶级菜单
	 *
	 * @param count
	 *            数量
	 * @return 顶级菜单
	 */
	List<Menu> findRoots(Integer count);

	/**
	 * 查找顶级菜单
	 *
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 顶级菜单
	 */
	List<Menu> findRoots(Integer count, boolean useCache);

	/**
	 * 查找上级菜单
	 *
	 * @param menu
	 *            菜单
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级菜单
	 */
	List<Menu> findParents(Menu menu, boolean recursive, Integer count);

	/**
	 * 查找上级菜单
	 *
	 * @param menuId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 上级菜单
	 */
	List<Menu> findParents(Long menuId, boolean recursive, Integer count, boolean useCache);

	/**
	 * 查找菜单树
	 *
	 * @return 菜单树
	 */
	List<Menu> findTree();

	/**
	 * 查找下级菜单
	 *
	 * @param menu
	 *            菜单
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级菜单
	 */
	List<Menu> findChildren(Menu menu, boolean recursive, Integer count);

	/**
	 * 查找下级菜单
	 *
	 * @param menuId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 下级菜单
	 */
	List<Menu> findChildren(Long menuId, boolean recursive, Integer count, boolean useCache);
  Page<Menu> findPage(Menu parent, Pageable pageable);

}
