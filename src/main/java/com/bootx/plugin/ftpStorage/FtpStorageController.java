
package com.bootx.plugin.ftpStorage;

import com.bootx.common.Message;
import com.bootx.controller.admin.BaseController;
import com.bootx.entity.PluginConfig;
import com.bootx.service.PluginConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller - FTP存储
 * 
 * @author blackboy
 * @version 1.0
 */
@RestController("adminFtpStorageController")
@RequestMapping("/api/storage_plugin/ftp_storage")
public class FtpStorageController extends BaseController {

	@Autowired
	private FtpStoragePlugin ftpStoragePlugin;
	@Autowired
	private PluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@PostMapping("/install")
	public Message install() {
		if (!ftpStoragePlugin.getIsInstalled()) {
			PluginConfig pluginConfig = new PluginConfig();
			pluginConfig.setPluginId(ftpStoragePlugin.getId());
			pluginConfig.setIsEnabled(false);
			pluginConfig.setAttributes(null);
			pluginConfigService.save(pluginConfig);
		}
		return Message.success("操作成功");
	}

	/**
	 * 卸载
	 */
	@PostMapping("/uninstall")
	public Message uninstall() {
		if (ftpStoragePlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(ftpStoragePlugin.getId());
		}
		return Message.success("操作成功");
	}

	/**
	 * 设置
	 */
	@PostMapping("/setting")
	public PluginConfig setting(ModelMap model) {
		return ftpStoragePlugin.getPluginConfig();
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public Message update(String host, Integer port, String username, String password, String urlPrefix, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
		PluginConfig pluginConfig = ftpStoragePlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<>();
		attributes.put("host", host);
		attributes.put("port", String.valueOf(port));
		attributes.put("username", username);
		attributes.put("password", password);
		attributes.put("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
		pluginConfig.setAttributes(attributes);
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrder(order);
		pluginConfigService.update(pluginConfig);
		return Message.success("操作成功");
	}

}