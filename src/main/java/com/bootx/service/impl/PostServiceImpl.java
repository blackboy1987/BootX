
package com.bootx.service.impl;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.dao.PostDao;
import com.bootx.entity.Department;
import com.bootx.entity.Post;
import com.bootx.service.PostService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Service - 角色
 *
 * @author blackboy
 * @version 1.0
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<Post, Long> implements PostService {

    @Resource
    private PostDao postDao;

    @Override
    public Page<Post> findPage(Pageable pageable, Department department, String name, String description, Integer level, Boolean isEnabled, Date beginDate, Date endDate) {
        return postDao.findPage(pageable,department,name,description,level,isEnabled,beginDate,endDate);
    }


    @Override
    @Transactional
    @CacheEvict(value = {"post"}, allEntries = true)
    public Post save(Post post) {
        return super.save(post);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"post"}, allEntries = true)
    public Post update(Post post) {
        return super.update(post);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"post"}, allEntries = true)
    public Post update(Post post, String... ignoreProperties) {
        return super.update(post, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"post"}, allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"post"}, allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"post"}, allEntries = true)
    public void delete(Post post) {
        super.delete(post);
    }

}
