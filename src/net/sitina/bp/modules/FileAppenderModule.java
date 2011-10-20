package net.sitina.bp.modules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class FileAppenderModule extends Module {

	protected String path = "";
	
	protected static String newLine = System.getProperty ("line.separator");
	
//	private long itemsProcessed = 0;
//	
//	public static final long ITEMS_PER_FILE = 5000;

	public FileAppenderModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void process(String item) {
//		if ((++itemsProcessed % ITEMS_PER_FILE) == 0) {
//			loadConfiguration();
//		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path), true));
			bw.write(item + newLine);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		out.putItem(item);
	}

	@Override
	protected void loadConfiguration() {
		if (configuration.containsKey("path")) {
			path = configuration.getStringProperty("path");
			
			try {
				File pathFile = new File(path);
				pathFile.createNewFile();
				File pathFileParentDirectory = new File(pathFile.getPath());
				
				if (!pathFileParentDirectory.exists()) {
					pathFileParentDirectory.mkdirs();
				}
				
				pathFile.delete();
			} catch (IOException e) {
				log.error("Error initializing path", e);
			}
		}
		
		if (configuration.containsKey("timestamp") && "true".equals(configuration.getStringProperty("timestamp"))) {
			path += "." + new Date().getTime() + "";
			try {
				Thread.sleep(1000); // to ensure that files will be named differently in case adding timestamp
			} catch (InterruptedException e) {
				log.error("Thread was interrupted", e);
			}
		}
		
		if (configuration.containsKey("extension")) {
			path += "." + configuration.getStringProperty("extension");
		}
		
	}

}
