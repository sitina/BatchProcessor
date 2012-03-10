package net.sitina.bp.impl;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import net.sitina.bp.api.Configuration;
import net.sitina.bp.api.ModuleConfiguration;

import org.apache.log4j.Logger;

public class PropertiesConfiguration implements Configuration {

	public static final String PROPERTIES_DELIMITER = ";";

	public static final String KEY_VALUE_DELIMITER = "|";

	protected static Logger log = Logger.getLogger(PropertiesConfiguration.class);

	protected List<ModuleConfiguration> modulesConfiguration = new ArrayList<ModuleConfiguration>();

	protected Properties properties = null;

	public PropertiesConfiguration() {
		this("config/test.properties");
	}

	public PropertiesConfiguration(String fileName) {
		properties = new Properties();

		try {
			properties.load(new FileReader(fileName));
			loadModules();
		} catch (Exception e) {
			log.error(e);
		}
	}

	private void loadModules() {
		for (int i = 0; i < 1000; i++) {
			if (properties.getProperty(String.format("bp.modules.%s.class", i)) != null) {
				modulesConfiguration.add(loadModuleConfiguration(i));
			}
		}
	}

	private ModuleConfiguration loadModuleConfiguration(int moduleIndex) {
		String modulePropertiesString = properties.getProperty(String.format("bp.modules.%s.properties", moduleIndex));
		Map<String, String> moduleProperties = loadModuleProperties(modulePropertiesString);
		String className = properties.getProperty(String.format("bp.modules.%s.class", moduleIndex));
		int instancesCount = 1;

		try {
			instancesCount = Integer.valueOf(properties.getProperty(String.format("bp.modules.%s.instances", moduleIndex))).intValue();
		} catch (Exception e) {
			log.error("Problem setting instances count for module #" + moduleIndex, e);
		}

		return new ModuleConfiguration(className, instancesCount, moduleProperties);
	}

	private Map<String, String> loadModuleProperties(String propertiesString) {
		Map<String, String> result = new HashMap<String, String>();

		if (propertiesString != null) {

			StringTokenizer tokenizer = new StringTokenizer(propertiesString, PROPERTIES_DELIMITER);

			while (tokenizer.hasMoreTokens()) {
				String keyValue = tokenizer.nextToken();
				StringTokenizer keyValueTokenizer = new StringTokenizer(keyValue, KEY_VALUE_DELIMITER);

				if (keyValueTokenizer.countTokens() > 1) {
					result.put(keyValueTokenizer.nextToken(), keyValueTokenizer.nextToken());
				}
			}
		}

		return result;
	}

	@Override
	public List<ModuleConfiguration> getModuleConfigurations() {
		return modulesConfiguration;
	}

}