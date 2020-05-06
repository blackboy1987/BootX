
package com.bootx.service.impl;

import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.dao.WordTypeDao;
import com.bootx.entity.WordType;
import com.bootx.service.WordTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Service - 角色
 *
 * @author blackboy
 * @version 1.0
 */
@Service
public class WordTypeServiceImpl extends BaseServiceImpl<WordType, Long> implements WordTypeService {

	@Autowired
	private WordTypeDao wordTypeDao;

	@Override
	@Transactional
	@CacheEvict(value = { "wordType","word" }, allEntries = true)
	public WordType save(WordType wordType) {
		return super.save(wordType);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "wordType","word"  }, allEntries = true)
	public WordType update(WordType wordType) {
		return super.update(wordType);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "wordType","word"  }, allEntries = true)
	public WordType update(WordType wordType, String... ignoreProperties) {
		return super.update(wordType, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "wordType","word"  }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "wordType","word"  }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "wordType","word"  }, allEntries = true)
	public void delete(WordType wordType) {
		super.delete(wordType);
	}

	@Override
	@Cacheable(value = "wordType")
	public List<WordType> findAll(){
		return super.findAll();
	}

	@Override
	public Page<WordType> findPage(Pageable pageable, String name,Boolean isEnabled, Date beginDate, Date endDate) {
		return wordTypeDao.findPage(pageable,name,isEnabled,beginDate,endDate);
	}
}
