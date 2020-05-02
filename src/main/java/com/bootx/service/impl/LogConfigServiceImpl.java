
package com.bootx.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.bootx.common.CommonAttributes;
import com.bootx.common.LogConfig;
import com.bootx.service.LogConfigService;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class LogConfigServiceImpl implements LogConfigService {

	@Cacheable("logConfig")
	public List<LogConfig> getAll() {
		try {
      InputStream settingXmlFile = new ClassPathResource(CommonAttributes.SETTING_XML_PATH).getInputStream();
      Document document = new SAXReader().read(settingXmlFile);
			List<org.dom4j.Node> nodes = document.selectNodes("/setting/logConfig");
			List<LogConfig> logConfigs = new ArrayList<LogConfig>();
			for (org.dom4j.Node node : nodes) {
			  org.dom4j.Element element = (org.dom4j.Element)node;
				String operation = element.attributeValue("operation");
				String urlPattern = element.attributeValue("urlPattern");
				LogConfig logConfig = new LogConfig();
				logConfig.setOperation(operation);
				logConfig.setUrlPattern(urlPattern);
				logConfigs.add(logConfig);
			}
			return logConfigs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
