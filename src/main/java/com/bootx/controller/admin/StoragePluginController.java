
package com.bootx.controller.admin;

import com.bootx.plugin.StoragePlugin;
import com.bootx.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller - 存储插件
 * 
 * @author IGOMALL  Team
 * @version 1.0
 */
@RestController("adminStoragePluginController")
@RequestMapping("/api/storage_plugin")
public class StoragePluginController extends BaseController {

	@Autowired
	private PluginService pluginService;

	/**
	 * 列表
	 */
	@PostMapping("/list")
	public List<StoragePlugin> list() {
		return pluginService.getStoragePlugins();
	}

	@PostMapping("/setting")
	public StoragePlugin setting(String storagePluginId) {
		return pluginService.getStoragePlugin(storagePluginId);
	}
}