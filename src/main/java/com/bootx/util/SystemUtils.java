
package com.bootx.util;

import com.bootx.common.CommonAttributes;
import com.bootx.common.EnumConverter;
import com.bootx.common.Setting;
import com.bootx.common.TemplateConfig;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean2;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.io.FileUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Utils - 系统
 *
 * @author blackboy
 * @version 1.0
 */
public final class SystemUtils {

	/**
	 * CacheManager
	 */
	private static final CacheManager CACHE_MANAGER = CacheManager.create();

	/**
	 * BeanUtilsBean
	 */
	private static final BeanUtilsBean BEAN_UTILS;

	static {
		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean2() {

			@Override
			public Converter lookup(Class<?> clazz) {
				Converter converter = super.lookup(clazz);
				if (converter != null) {
					return converter;
				}

				if (clazz.isEnum()) {
					EnumConverter enumConverter = new EnumConverter(clazz);
					super.register(enumConverter, clazz);
					return enumConverter;
				}
				if (clazz.isArray()) {
					Converter componentConverter = lookup(clazz.getComponentType());
					if (componentConverter != null) {
						ArrayConverter arrayConverter = new ArrayConverter(clazz, componentConverter, 0);
						arrayConverter.setOnlyFirstToString(false);
						super.register(arrayConverter, clazz);
						return arrayConverter;
					}
				}
				return super.lookup(clazz);
			}

		};

		DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(CommonAttributes.DATE_PATTERNS);
		convertUtilsBean.register(dateConverter, Date.class);

		BEAN_UTILS = new BeanUtilsBean(convertUtilsBean);
	}

	/**
	 * 不可实例化
	 */
	private SystemUtils() {
	}

	/**
	 * 获取系统设置
	 *
	 * @return 系统设置
	 */
	@SuppressWarnings("unchecked")
	public static Setting getSetting() {
		Ehcache cache = CACHE_MANAGER.getEhcache(Setting.CACHE_NAME);
		String cacheKey = "setting";
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			Setting setting = new Setting();
			try {
				InputStream settingXmlFile = new ClassPathResource(CommonAttributes.SETTING_XML_PATH).getInputStream();
				Document document = new SAXReader().read(settingXmlFile);
				List<org.dom4j.Node> nodes = document.selectNodes("/setting/settingConfig");
				for (org.dom4j.Node node: nodes) {
					try {
						org.dom4j.Element element = (org.dom4j.Element)node;
						String name = element.attributeValue("name");
						String value = element.attributeValue("value");
						BEAN_UTILS.setProperty(setting, name, value);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, setting));
			cacheElement = cache.get(cacheKey);
		}
		return (Setting) cacheElement.getObjectValue();
	}

	/**
	 * 设置系统设置
	 *
	 * @param setting
	 *            系统设置
	 */
	@SuppressWarnings("unchecked")
	public static void setSetting(Setting setting) {
		Assert.notNull(setting,"");

		try {
			InputStream settingXmlFile = new ClassPathResource(CommonAttributes.SETTING_XML_PATH).getInputStream();
			Document document = new SAXReader().read(settingXmlFile);
			List<org.dom4j.Node> nodes = document.selectNodes("/setting/settingConfig");
			for (org.dom4j.Node node : nodes) {
				try {
					org.dom4j.Element element = (org.dom4j.Element)node;
					String name = element.attributeValue("name");
					String value = BEAN_UTILS.getProperty(setting, name);
					Attribute attribute = element.attribute("value");
					attribute.setValue(value);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}

			XMLWriter xmlWriter = null;
			try {
				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
				outputFormat.setEncoding("UTF-8");
				outputFormat.setIndent(true);
				outputFormat.setIndent("	");
				outputFormat.setNewlines(true);
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".xml");
				FileUtils.copyInputStreamToFile(settingXmlFile,tempFile);
				xmlWriter = new XMLWriter(new FileOutputStream(tempFile), outputFormat);
				xmlWriter.write(document);
				xmlWriter.flush();
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				try {
					if (xmlWriter != null) {
						xmlWriter.close();
					}
				} catch (IOException e) {
				}
			}
			Ehcache cache = CACHE_MANAGER.getEhcache(Setting.CACHE_NAME);
			String cacheKey = "setting";
			cache.put(new Element(cacheKey, setting));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (DocumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获取模板配置
	 *
	 * @param id
	 *            ID
	 * @return 模板配置
	 */
	public static TemplateConfig getTemplateConfig(String id) {
		Assert.hasText(id,"");

		Ehcache cache = CACHE_MANAGER.getEhcache(TemplateConfig.CACHE_NAME);
		String cacheKey = "templateConfig_" + id;
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			TemplateConfig templateConfig = null;
			try {
				InputStream settingXmlFile = new ClassPathResource(CommonAttributes.SETTING_XML_PATH).getInputStream();
				Document document = new SAXReader().read(settingXmlFile);
				org.dom4j.Element element = (org.dom4j.Element) document.selectSingleNode("/setting/templateConfig[@id='" + id + "']");
				if (element != null) {
					templateConfig = getTemplateConfig(element);
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, templateConfig));
			cacheElement = cache.get(cacheKey);
		}
		return (TemplateConfig) cacheElement.getObjectValue();
	}

	/**
	 * 获取模板配置
	 *
	 * @param type
	 *            类型
	 * @return 模板配置
	 */
	@SuppressWarnings("unchecked")
	public static List<TemplateConfig> getTemplateConfigs(TemplateConfig.Type type) {
		Ehcache cache = CACHE_MANAGER.getEhcache(TemplateConfig.CACHE_NAME);
		String cacheKey = "templateConfigs_" + type;
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			List<TemplateConfig> templateConfigs = new ArrayList<>();
			try {
				File settingXmlFile = new ClassPathResource(CommonAttributes.SETTING_XML_PATH).getFile();
				Document document = new SAXReader().read(settingXmlFile);
				List<org.dom4j.Node> nodes = document.selectNodes(type != null ? "/setting/templateConfig[@type='" + type + "']" : "/setting/templateConfig");
				for (org.dom4j.Node node : nodes) {
					templateConfigs.add(getTemplateConfig((org.dom4j.Element)node));
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, templateConfigs));
			cacheElement = cache.get(cacheKey);
		}
		return (List<TemplateConfig>) cacheElement.getObjectValue();
	}

	/**
	 * 获取所有模板配置
	 *
	 * @return 所有模板配置
	 */
	public static List<TemplateConfig> getTemplateConfigs() {
		return getTemplateConfigs(null);
	}

	/**
	 * 获取模板配置
	 *
	 * @param element
	 *            元素
	 * @return 模板配置
	 */
	private static TemplateConfig getTemplateConfig(org.dom4j.Element element) {
		Assert.notNull(element,"");

		String id = element.attributeValue("id");
		String type = element.attributeValue("type");
		String name = element.attributeValue("name");
		String templatePath = element.attributeValue("templatePath");
		String description = element.attributeValue("description");

		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setId(id);
		templateConfig.setType(TemplateConfig.Type.valueOf(type));
		templateConfig.setName(name);
		templateConfig.setTemplatePath(templatePath);
		templateConfig.setDescription(description);
		return templateConfig;
	}

}
