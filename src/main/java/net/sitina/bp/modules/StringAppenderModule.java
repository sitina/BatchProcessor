package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class StringAppenderModule extends Module {

	protected String pattern = "";
	
	public StringAppenderModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void loadConfiguration() {
		if (configuration.containsKey("pattern")) {
			pattern = configuration.getStringProperty("pattern");
		}
	}

	@Override
	protected void process(String item) {
		try {
		    item = item.replace(" ", "+");
			out.putItem(String.format(pattern, item, item));
		} catch (Exception e) {
			out.putItem(item);
		}
	}

}
