package net.sitina.bp;

import net.sitina.bp.core.ModulesRunner;

public class BatchProcessor {

	private static String configFile;
	
	public static void main(String[] args) {
		
		// configFile = "config/bpCZSODownload.properties";

		// configFile = "config/bpCZSOProcess.properties";
		
		// configFile = "config/bpCZSOMergeResults.properties";
		
		configFile = "config/bpCZSOSaveResults.properties";
		
		// configFile = "config/test.properties";
		
		if (args != null && args.length > 0) {
			configFile = args[0];
		}
		
		ModulesRunner.main(new String[]{configFile});
	}
	
}
