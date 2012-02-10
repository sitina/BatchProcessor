package net.sitina.bp.modules;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class CZSOExtractor extends Module {

	private String fileName = "CZSO.csv";
	
	public CZSOExtractor(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void loadConfiguration() {		
		String loadedFileName = configuration.getStringProperty("fileName");
		
		if (loadedFileName != null) {
			fileName = loadedFileName;
		}		
	}

	@Override
	protected void process(String item) {
		String pageContents = getPage(item);
		
		try {
			if (pageContents != null && pageContents.length() > 0) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource( new StringReader( pageContents ) );
				Document d = builder.parse( is );
			}
		} catch (Exception e) {
			log.error(e);
		}
		
		return;
	}
	
	private String getPage(String address) {
		OutputStream out = null;
		URLConnection conn = null;
		InputStream  in = null;
		String result = null;
		try {
			URL url = new URL(address);
			ByteArrayOutputStream bbb = new ByteArrayOutputStream();
			out = new BufferedOutputStream(bbb);
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024 * 8];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			log.debug("download > " + address + "\t" + numWritten);
			
			result = new String(out.toString());
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
				log.equals(ioe);
			}
		}
		
		return result;
	}

}
