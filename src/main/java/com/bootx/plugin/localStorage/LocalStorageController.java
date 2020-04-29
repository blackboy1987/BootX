
package com.bootx.plugin.localStorage;

import com.bootx.common.Message;
import com.bootx.controller.admin.BaseController;
import com.bootx.entity.PluginConfig;
import com.bootx.service.PluginConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 本地文件存储
 * 
 * @author blackboy
 * @version 1.0
 */
@RestController("adminLocalStorageController")
@RequestMapping("/api/storage_plugin/local_storage")
public class LocalStorageController extends BaseController {

	@Autowired
	private LocalStoragePlugin localStoragePlugin;
	@Autowired
	private PluginConfigService pluginConfigService;

	/**
	 * 设置
	 */
	@PostMapping("/setting")
	public PluginConfig setting() {
		return localStoragePlugin.getPluginConfig();
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public Message update(Integer order, RedirectAttributes redirectAttributes) {
		PluginConfig pluginConfig = localStoragePlugin.getPluginConfig();
		pluginConfig.setIsEnabled(true);
		pluginConfig.setOrder(order);
		pluginConfigService.update(pluginConfig);
		return SUCCESS_MESSAGE;
	}

}