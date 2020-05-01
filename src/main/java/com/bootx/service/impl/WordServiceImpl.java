
package com.bootx.service.impl;

import com.bootx.dao.WordDao;
import com.bootx.entity.Word;
import com.bootx.service.WordService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service - 角色
 *
 * @author blackboy
 * @version 1.0
 */
@Service
public class WordServiceImpl extends BaseServiceImpl<Word, Long> implements WordService {

  @Autowired
  private WordDao wordDao;

	@Override
	@Transactional
	@CacheEvict(value = { "wordType","word" }, allEntries = true)
	public Word save(Word word) {
		return super.save(word);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "wordType","word"  }, allEntries = true)
	public Word update(Word word) {
    Word pWord = super.update(word);
    if (BooleanUtils.isTrue(pWord.getIsDefault())) {
      wordDao.clearDefault(pWord);
    }

    return pWord;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "wordType","word"  }, allEntries = true)
	public Word update(Word word, String... ignoreProperties) {
    Word pWord = super.update(word,ignoreProperties);
    if (BooleanUtils.isTrue(pWord.getIsDefault())) {
      wordDao.clearDefault(pWord);
    }

		return pWord;
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
	public void delete(Word word) {
		super.delete(word);
	}

	@Override
	@Cacheable(value = "role")
	public List<Word> findAll(){
		return super.findAll();
	}

}
