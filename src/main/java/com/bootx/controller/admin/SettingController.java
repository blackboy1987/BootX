
package com.bootx.controller.admin;

import com.bootx.common.FileType;
import com.bootx.common.Setting;
import com.bootx.service.CacheService;
import com.bootx.service.FileService;
import com.bootx.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller - 系统设置
 *
 * @author blackboy
 * @version 1.0
 */
@RestController("adminstingController")
@RequestMapping("/api/setting")
public class SettingController extends BaseController {

	@Autowired
	private FileService fileService;
	@Autowired
	private CacheService cacheService;

	/**
	 * 编辑
	 */
	@PostMapping("/edit")
	public Map<String,Object> edit() {
	  Map<String,Object> data = new HashMap<>();
    data.put("setting", SystemUtils.getSetting());
    data.put("locales", Setting.Locale.values());
    data.put("watermarkPositions", Setting.WatermarkPosition.values());
    data.put("roundTypes", Setting.RoundType.values());
    data.put("registerTypes", Setting.RegisterType.values());
    data.put("captchaTypes", Setting.CaptchaType.values());
    data.put("stockAllocationTimes", Setting.StockAllocationTime.values());
		return data;
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public String update(Setting setting, MultipartFile watermarkImageFile, RedirectAttributes redirectAttributes) {
		if (!isValid(setting)) {
			return ERROR_VIEW;
		}
		if (setting.getDefaultPointScale() > setting.getMaxPointScale()) {
			return ERROR_VIEW;
		}
		Setting srcSetting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getSmtpPassword())) {
			setting.setSmtpPassword(srcSetting.getSmtpPassword());
		}
		if (watermarkImageFile != null && !watermarkImageFile.isEmpty()) {
			if (!fileService.isValid(FileType.image, watermarkImageFile)) {
				return "redirect:edit";
			}
			String watermarkImage = fileService.uploadLocal(FileType.image, watermarkImageFile);
			setting.setWatermarkImage(watermarkImage);
		} else {
			setting.setWatermarkImage(srcSetting.getWatermarkImage());
		}
		if (StringUtils.isEmpty(setting.getSmsSn()) || StringUtils.isEmpty(setting.getSmsKey())) {
			setting.setSmsSn(null);
			setting.setSmsKey(null);
		}
		setting.setIsCnzzEnabled(srcSetting.getIsCnzzEnabled());
		setting.setCnzzSiteId(srcSetting.getCnzzSiteId());
		setting.setCnzzPassword(srcSetting.getCnzzPassword());

		SystemUtils.setSetting(setting);
		cacheService.clear();
		return "redirect:edit";
	}

}
