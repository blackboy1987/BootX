
package com.bootx.service.impl;

import com.bootx.service.CacheService;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * Service - 缓存
 * 
 * @author blackboy
 * @version 1.0
 */
@Service
public class CacheServiceImpl implements CacheService {

	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Autowired
	private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

	@Override
	public String getDiskStorePath() {
		return cacheManager.getConfiguration().getDiskStoreConfiguration().getPath();
	}

	@Override
	public int getCacheSize() {
		int cacheSize = 0;
		String[] cacheNames = cacheManager.getCacheNames();
		if (cacheNames != null) {
			for (String cacheName : cacheNames) {
				Ehcache cache = cacheManager.getEhcache(cacheName);
				if (cache != null) {
					cacheSize += cache.getSize();
				}
			}
		}
		return cacheSize;
	}

	@Override
	@CacheEvict(value = { "setting", "templateConfig", "pluginConfig", "messageConfig", "area", "seo", "adPosition", "memberAttribute", "navigation", "friendLink", "brand", "attribute", "article", "articleCategory", "articleTag", "product", "productCategory", "productTag", "review", "consultation",
			"promotion", "sitemap", "commonJs", "transitSteps", "authorization", "businessAttribute", "storeProductCategory", "productFavorite", "storeFavorite" }, allEntries = true)
	public void clear() {
		freeMarkerConfigurer.getConfiguration().clearTemplateCache();
		reloadableResourceBundleMessageSource.clearCache();
	}

}