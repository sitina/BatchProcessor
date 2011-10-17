package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.czso.ICOValidator;

public class ICOValidatorModule extends Module {

	public ICOValidatorModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void loadConfiguration() {
		// nothing to do here
	}

	@Override
	protected void process(String item) {
		if (ICOValidator.validateICO(item)) {
			out.putItem(item);
		}
	}

}
