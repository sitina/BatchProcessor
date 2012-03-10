package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class LoopModule extends Module {

	private String inputText = "";
	
	private int start = 0;
	
	private int end = 0;
	
	private int step = 1;
	
	public LoopModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void process(String item) {
		if (inputText != null) {
			item = inputText;
		}
		
		for (long i = start; (step > 0) ? (i <= end) : (i >= end); i += step) {
			out.putItem(String.format(inputText, i));
		}
	}

	@Override
	protected void loadConfiguration() {
		start = configuration.getIntProperty("start");
		end = configuration.getIntProperty("end");
		step = configuration.getIntProperty("step");
		inputText = configuration.getStringProperty("inputText");
	}

}
