package net.sitina.bp.modules;

import java.util.ArrayList;
import java.util.List;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.api.ModuleParameter;
import net.sitina.bp.util.BPStringTokenizer;

public class StartModule extends Module {

	private static final String START_ITEMS = "startItems";
	private static final String DELIMITER = ",";
	
	@ModuleParameter(required=true, description="list of items to be processed further on")
	private List<String> startItems;
	
	public StartModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void process(String item) {
		for (String itm : startItems) {
			out.putItem(itm);
		}
	}

	@Override
	protected void loadConfiguration() {
		startItems = new ArrayList<String>();
		
		String tmp = configuration.getStringProperty(START_ITEMS);
		BPStringTokenizer strTok = new BPStringTokenizer(tmp, DELIMITER);
		
		while (strTok.hasMoreTokens()) {
			startItems.add(strTok.nextToken());
		}
	}

}
