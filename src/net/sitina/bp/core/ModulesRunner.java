package net.sitina.bp.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.sitina.bp.api.Configuration;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;
import net.sitina.bp.impl.PropertiesConfiguration;

import org.apache.log4j.Logger;

public class ModulesRunner extends Thread {

	protected Logger log = Logger.getLogger(ModulesRunner.class);
	
	protected Configuration config = null;
	
	protected List<Thread> threadList = new ArrayList<Thread>();
	
	public static void main(String[] args) {
		Configuration c = new PropertiesConfiguration();
		
		if (args != null && args.length > 0) {
			c = new PropertiesConfiguration(args[0]);
		}
		
		ModulesRunner mr = new ModulesRunner(c);
		mr.start();
	}
	
	public ModulesRunner(Configuration config) {
		this.config = config;
	}
	
	public void run() {
		List<ModuleConfiguration> moduleConfigurations = config.getModuleConfigurations();
		
		Hub inHub = getHubInstance();
		for (int i = 0; i < moduleConfigurations.get(0).getInstancesCount(); i++) {
			inHub.putItem("startup");
		}
		inHub.setComplete();
		
		for (ModuleConfiguration mc : moduleConfigurations) {
			Hub outHub = getHubInstance();
			startModule(inHub, outHub, mc);
			inHub = outHub;
			outHub = getHubInstance();
		}
	}
	
	private void startModule(Hub in, Hub out, ModuleConfiguration mc) {
		for (int i = 0; i < mc.getInstancesCount(); i++) {
			try {
				log.debug("Instantiating module " + mc.getClassName() + ", instance # " + i);
				Class<? extends Module> c = Class.forName(mc.getClassName()).asSubclass(Module.class);
				Constructor<?>[] constructors = c.getConstructors();
				Module m = (Module)constructors[0].newInstance(in, out, mc, i);
				Thread t = new Thread(m);
				t.setName(mc.getClassName() + " " + (i + 1));
				t.start();
				threadList.add(t);
				log.debug("module " + mc.getClassName() + ", instance # " + i + " started");
			} catch (Exception e) {
				log.error(e);
			}
		}
	}
	
	private Hub getHubInstance() {
		return new InMemoryHub();
	} 
	
}
