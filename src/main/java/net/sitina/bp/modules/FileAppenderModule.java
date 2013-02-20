package net.sitina.bp.modules;

import net.sitina.bp.api.BatchProcessorException;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class FileAppenderModule extends Module {

	private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

	protected String path = "";
	
	protected static String newLine = System.getProperty ("line.separator");

	public FileAppenderModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void process(String item) {
		try {
			File f = new File(path);
			if (f.exists() && f.length() > MAX_FILE_SIZE) {
				loadConfiguration();
				f = new File(path);
				f.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
			bw.write(item + newLine);
			bw.close();
		} catch (IOException e) {
			throw new BatchProcessorException(this.getClass(), item, e);
		}
		
		out.putItem(item);
	}

	@Override
	protected void loadConfiguration() {
		if (configuration.containsKey("path")) {
			path = configuration.getStringProperty("path");
			
			try {
				File pathFile = new File(path);
				pathFile.mkdirs();
				pathFile.delete();
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
