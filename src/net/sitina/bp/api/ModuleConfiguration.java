package net.sitina.bp.api;

import java.util.Hashtable;
import java.util.Set;

import org.apache.log4j.Logger;

public class ModuleConfiguration {

	protected static Logger log = Logger.getLogger(ModuleConfiguration.class);
	
	private String className;
	
	private int instancesCount;
	
	private Hashtable<String, String> properties;
	
	public ModuleConfiguration(String className, int instancesCount, Hashtable<String, String> properties) {
		super();
		this.className = className;
		this.instancesCount = instancesCount;
		this.properties = properties;
	}

	public String getClassName() {
		return className;
	}

	public int getInstancesCount() {
		return instancesCount;
	}

	public Set<String> getProperties() {
		return properties.keySet();
	}
	
	public boolean containsKey(String key) {
		return properties != null && properties.containsKey(key);
	}
	
	public String getStringProperty(String name) {
		if (properties.get(name) != null) {
			return properties.get(name).toString();
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return "ModuleConfiguration [className=" + className
				+ ", instancesCount=" + instancesCount + ", properties="
				+ properties + "]";
	}

	public int getIntProperty(String name) {
		String val = properties.get(name);
		if (val != null) {
			try {
				return Integer.valueOf(val).intValue();
			} catch (Exception e) {
				log.error("Unable to read int. property " + name);
			}
		}
		
		return 0;
	}
	
}
