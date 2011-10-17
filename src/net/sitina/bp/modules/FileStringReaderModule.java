package net.sitina.bp.modules;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class FileStringReaderModule extends Module {

	public FileStringReaderModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void process(String item) {
		File f = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			f = new File(item);
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);
			isr = new InputStreamReader(bis);
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				out.putItem(line);
			}
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				log.error(e);
			}
			try {
				isr.close();
			} catch (Exception e) {
				log.error(e);
			}
			try {
				bis.close();
			} catch (Exception e) {
				log.error(e);
			}
			try {
				fis.close();
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	@Override
	protected void loadConfiguration() {
		// TODO Auto-generated method stub
		
	}

}
