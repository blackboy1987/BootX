
package com.bootx.plugin.ossStorage;

import com.bootx.common.Message;
import com.bootx.controller.admin.BaseController;
import com.bootx.entity.PluginConfig;
import com.bootx.service.PluginConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller - 阿里云存储
 * 
 * @author blackboy
 * @version 1.0
 */
@RestController("adminOssStorageController")
@RequestMapping("/api/storage_plugin/oss_storage")
public class OssStorageController extends BaseController {

	@Autowired
	private OssStoragePlugin ossStoragePlugin;
	@Autowired
	private PluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@PostMapping("/install")
	public Message install() {
		if (!ossStoragePlugin.getIsInstalled()) {
			PluginConfig pluginConfig = new PluginConfig();
			pluginConfig.setPluginId(ossStoragePlugin.getId());
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
		if (ossStoragePlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(ossStoragePlugin.getId());
		}
		return Message.success("操作成功");
	}

	/**
	 * 设置
	 */
	@PostMapping("/setting")
	public PluginConfig setting() {
		return ossStoragePlugin.getPluginConfig();
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public Message update(String endpoint, String accessId, String accessKey, String bucketName, String urlPrefix,String isTransform,String mpsRegionId,String pipelineId,String templateId,String ossLocation,String transformCallback, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order) {
		PluginConfig pluginConfig = ossStoragePlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<>();
		attributes.put("endpoint", endpoint);
		attributes.put("accessId", accessId);
		attributes.put("accessKey", accessKey);
		attributes.put("bucketName", bucketName);
		attributes.put("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
		pluginConfig.setAttributes(attributes);
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrder(order);
		pluginConfigService.update(pluginConfig);
		return Message.success("操作成功");
	}
}