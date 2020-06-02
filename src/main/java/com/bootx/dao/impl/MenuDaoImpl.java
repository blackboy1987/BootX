
package com.bootx.dao.impl;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.dao.MenuDao;
import com.bootx.entity.Menu;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Dao - 菜单
 *
 * @author blackboy
 * @version 1.0
 */
@Repository
public class MenuDaoImpl extends BaseDaoImpl<Menu, Long> implements MenuDao {


    @Override
    public List<Menu> findRoots(Integer count) {
        String jpql = "select menu from Menu menu where menu.parent is null order by menu.order asc";
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);
        if (count != null) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }

    @Override
    public List<Menu> findParents(Menu menu, boolean recursive, Integer count) {
        if (menu == null || menu.getParent() == null) {
            return Collections.emptyList();
        }
        TypedQuery<Menu> query;
        if (recursive) {
            String jpql = "select menu from Menu menu where menu.id in (:ids) order by menu.grade asc";
            query = entityManager.createQuery(jpql, Menu.class).setParameter("ids", Arrays.asList(menu.getParentIds()));
        } else {
            String jpql = "select menu from Menu menu where menu = :menu";
            query = entityManager.createQuery(jpql, Menu.class).setParameter("menu", menu.getParent());
        }
        if (count != null) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }

    @Override
    public List<Menu> findChildren(Menu menu, boolean recursive, Integer count) {
        TypedQuery<Menu> query;
        if (recursive) {
            if (menu != null) {
                String jpql = "select menu from Menu menu where menu.treePath like :treePath order by menu.grade asc, menu.order asc";
                query = entityManager.createQuery(jpql, Menu.class).setParameter("treePath", "%" + Menu.TREE_PATH_SEPARATOR + menu.getId() + Menu.TREE_PATH_SEPARATOR + "%");
            } else {
                String jpql = "select menu from Menu menu order by menu.grade asc, menu.order asc";
                query = entityManager.createQuery(jpql, Menu.class);
            }
            if (count != null) {
                query.setMaxResults(count);
            }
            List<Menu> result = query.getResultList();
            sort(result);
            return result;
        } else {
            String jpql = "select menu from Menu menu where menu.parent = :parent order by menu.order asc";
            query = entityManager.createQuery(jpql, Menu.class).setParameter("parent", menu);
            if (count != null) {
                query.setMaxResults(count);
            }
            return query.getResultList();
        }
    }

    /**
     * 排序菜单
     *
     * @param menus 菜单
     */
    private void sort(List<Menu> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }
        final Map<Long, Integer> orderMap = new HashMap<>();
        for (Menu menu : menus) {
            orderMap.put(menu.getId(), menu.getOrder());
        }
        Collections.sort(menus, new Comparator<Menu>() {
            @Override
            public int compare(Menu menu1, Menu menu2) {
                Long[] ids1 = (Long[]) ArrayUtils.add(menu1.getParentIds(), menu1.getId());
                Long[] ids2 = (Long[]) ArrayUtils.add(menu2.getParentIds(), menu2.getId());
                Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
                Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
                CompareToBuilder compareToBuilder = new CompareToBuilder();
                while (iterator1.hasNext() && iterator2.hasNext()) {
                    Long id1 = iterator1.next();
                    Long id2 = iterator2.next();
                    Integer order1 = orderMap.get(id1);
                    Integer order2 = orderMap.get(id2);
                    compareToBuilder.append(order1, order2).append(id1, id2);
                    if (!iterator1.hasNext() || !iterator2.hasNext()) {
                        compareToBuilder.append(menu1.getGrade(), menu2.getGrade());
                    }
                }
                return compareToBuilder.toComparison();
            }
        });
    }

    @Override
    public Page<Menu> findPage(Menu parent, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Menu> criteriaQuery = criteriaBuilder.createQuery(Menu.class);
        Root<Menu> root = criteriaQuery.from(Menu.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (parent != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parent"), parent));
        } else {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("parent")));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
}
