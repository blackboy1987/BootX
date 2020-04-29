
package com.bootx.service.impl;

import com.bootx.plugin.StoragePlugin;
import com.bootx.service.PluginService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service - 插件
 * 
 * @author blackboy
 * @version 1.0
 */
@Service
public class PluginServiceImpl implements PluginService {

	@Autowired
	private List<StoragePlugin> storagePlugins = new ArrayList<>();

	@Autowired
	private Map<String, StoragePlugin> storagePluginMap = new HashMap<>();

	@Override
	public List<StoragePlugin> getStoragePlugins() {
		Collections.sort(storagePlugins);
		return storagePlugins;
	}

	@Override
	public List<StoragePlugin> getStoragePlugins(final boolean isEnabled) {
		List<StoragePlugin> result = new ArrayList<>();
		CollectionUtils.select(storagePlugins, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				StoragePlugin storagePlugin = (StoragePlugin) object;
				return storagePlugin.getIsEnabled() == isEnabled;
			}
		}, result);
		Collections.sort(result);
		return result;
	}

	@Override
	public StoragePlugin getStoragePlugin(String id) {
		return storagePluginMap.get(id);
	}

}