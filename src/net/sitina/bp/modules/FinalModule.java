package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class FinalModule extends Module {
	
	private long count = 0;
	
	private long processed = 0;

	public FinalModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
	}

	@Override
	protected void loadConfiguration() {
	}

	@Override
	protected void process(String item) {
		if ((++count % 1000) == 0) {
			count = 0;
			log.info("Processed "+ ++processed +" 000 items (last item: '" + item + "')");
		}
	}

}
