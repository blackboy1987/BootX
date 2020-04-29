
package com.bootx.plugin.localStorage;

import com.bootx.common.Message;
import com.bootx.controller.admin.BaseController;
import com.bootx.entity.PluginConfig;
import com.bootx.service.PluginConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller - 本地文件存储
 * 
 * @author blackboy
 * @version 1.0
 */
@Controller("adminLocalStorageController")
@RequestMapping("/api/storage_plugin/local_storage")
public class LocalStorageController extends BaseController {

	@Autowired
	private LocalStoragePlugin localStoragePlugin;
	@Autowired
	private PluginConfigService pluginConfigService;

	/**
	 * 设置
	 */
	@GetMapping("/setting")
	public PluginConfig setting(ModelMap model) {
		return localStoragePlugin.getPluginConfig();
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public Message update(Integer order, String uploadPath, String urlPrefix) {
		PluginConfig pluginConfig = localStoragePlugin.getPluginConfig();
		pluginConfig.setIsEnabled(true);
		pluginConfig.setOrder(order);
		Map<String, String> attributes = new HashMap<>();
		attributes.put("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
		attributes.put("uploadPath", StringUtils.removeEnd(uploadPath, "/"));
		pluginConfig.setAttributes(attributes);
		pluginConfigService.update(pluginConfig);
		return Message.success("操作成功");
	}

}