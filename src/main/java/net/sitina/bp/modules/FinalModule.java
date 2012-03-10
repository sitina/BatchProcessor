package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class FinalModule extends Module {
	
	private static final String LOG_AMOUNT_PARAMETER = "logAmount";

	private long count = 0;
	
	private long processed = 0;
	
	private long logAmount = 1000;

	public FinalModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
	}

	@Override
	protected void loadConfiguration() {
		if (configuration.containsKey(LOG_AMOUNT_PARAMETER)) {
			logAmount = configuration.getIntProperty(LOG_AMOUNT_PARAMETER);
		}
	}

	@Override
	protected void process(String item) {
		if ((++count % logAmount) == 0) {
			count = 0;
			log.info("Processed "+ (++processed * logAmount) +" items (last item: '" + item + "')");
		}
	}

}
