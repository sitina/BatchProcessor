package net.sitina.bp.modules;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import net.sitina.bp.api.BatchProcessorException;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

/**
 * Module that downloads file from supplied parameter
 * 
 * @author jirka
 *
 */
public class FileDownloaderModule extends Module {

	protected String storagePath = "";
	
	protected String address;

	protected String localFileName;
	
	protected String extension = "";
	
	private static final long TIME_TO_WAIT = 30000;
	
	protected ArrayList<String> excluded = new ArrayList<String>();
	
	public FileDownloaderModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void loadConfiguration() {
		if (configuration.containsKey("path")) {
			storagePath = configuration.getStringProperty("path");
			
			File path = new File(storagePath);
			if (!path.exists()) {
				path.mkdirs();
			}
		}
		
		if (configuration.containsKey("exclude")) {
			StringTokenizer strTok = new StringTokenizer(configuration.getStringProperty("exclude"), ",");
			while (strTok.hasMoreTokens()) {
				excluded.add(strTok.nextToken());
			}
		}

		if (configuration.containsKey("extension")) {
			extension = configuration.getStringProperty("extension");
		}
	}

	@Override
	protected void process(String item) {		
		try {
			out.putItem(download(item));
		} catch (Exception e) {
			try {
				// the execution usually falls because of network unreachability
				// so we try to wait a little bit and give it another try
				log.error("Exception occured; trying to wait and download again", e);
				Thread.sleep(TIME_TO_WAIT);
				out.putItem(download(item));
			} catch (Exception e1) {
				throw new BatchProcessorException(this.getClass(), item, e1);
			}
		}
	}
	
	private String download(String address) throws Exception {
		int lastSlashIndex = address.lastIndexOf('/');
		int firstQuote = java.lang.Math.max(address.indexOf('?'), address.length());
		if (lastSlashIndex >= 0 && lastSlashIndex < address.length() - 1) {
			return download(address, address.substring(lastSlashIndex + 1, firstQuote));
		} else {
			log.error("Could not figure out local file name for " + address);
		}
		
		return null;
	}
	
	private String download(String address, String localFileName) throws Exception {
		
		for (int i = 0; i < excluded.size(); i++) {
			if (localFileName.contains(excluded.get(i))) {
				return null;
			}
		}
		
		localFileName = localFileName.replace('?', '-');
		
		this.address = address;
		this.localFileName = localFileName;
		String localCompletePath;
		if (extension != null && extension.length() > 0) {
			localCompletePath = storagePath + "/" + getFolderName(address) + "/" + localFileName + "." + extension;
		} else {
			localCompletePath = storagePath + "/" + getFolderName(address) + "/" + localFileName;
		}
		File pathTest = new File(storagePath + "/" + getFolderName(address));
		if (!pathTest.exists()) {
			pathTest.mkdirs();
		}
		
		File existTest = new File(localCompletePath);
		if (existTest.exists()) {
			log.debug("File already exists (filename '" + localCompletePath + "')");
			return null;
		}
		
		OutputStream out = null;
		URLConnection conn = null;
		InputStream  in = null;
		try {
			URL url = new URL(address);
			out = new BufferedOutputStream(
				new FileOutputStream(localCompletePath));
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024 * 8];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			log.debug("Downloaded " + localFileName + "\t" + numWritten);
		} catch (Exception exception) {
			throw new Exception("Problem downloading", exception);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
			}
		}
		
		return localCompletePath;
	}
	
	private String getFolderName(String address) {
		int lastSlashIndex = address.lastIndexOf('/');
		int oneButLastSlashIndex = address.substring(0, lastSlashIndex).lastIndexOf('/');
		return address.substring(oneButLastSlashIndex + 1, lastSlashIndex);
	}

}
