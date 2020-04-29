
package com.bootx.plugin.localStorage;

import com.bootx.common.Message;
import com.bootx.controller.admin.BaseController;
import com.bootx.entity.BaseEntity;
import com.bootx.entity.PluginConfig;
import com.bootx.service.PluginConfigService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	@PostMapping("/setting")
  @JsonView(BaseEntity.EditView.class)
	public PluginConfig setting() {
		return localStoragePlugin.getPluginConfig();
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public Message update(Integer order) {
		PluginConfig pluginConfig = localStoragePlugin.getPluginConfig();
		pluginConfig.setIsEnabled(true);
		pluginConfig.setOrder(order);
		pluginConfigService.update(pluginConfig);
		return Message.success("操作成功");
	}

}
