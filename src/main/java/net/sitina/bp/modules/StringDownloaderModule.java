package net.sitina.bp.modules;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import net.sitina.bp.api.BatchProcessorException;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class StringDownloaderModule extends Module {

	public StringDownloaderModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void process(String item) {
		StringBuilder sb = new StringBuilder();
		BufferedReader bufferedReader = null;
		
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new URL(item).openConnection().getInputStream()));
			
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (Exception e) {
			throw new BatchProcessorException(this.getClass(), item, e);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {					
				}
			}
		}
	
		out.putItem(sb.toString());
	}

	@Override
	protected void loadConfiguration() {
		// intentionally left blank
	}

}
