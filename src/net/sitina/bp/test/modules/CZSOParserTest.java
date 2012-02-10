package net.sitina.bp.test.modules;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;

import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;
import net.sitina.bp.modules.CZSOParserModule;
import net.sitina.bp.modules.CZSOParserModuleBackup;
import net.sitina.bp.test.BatchProcessorTestBase;

import org.junit.Test;

public class CZSOParserTest extends BatchProcessorTestBase {

	@Override
	public void setUp() throws Exception {
		in = new InMemoryHub();
		out = new InMemoryHub();
		in.setComplete();
		
		String rootPath = "testData";
		
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("path", rootPath);

		config = new ModuleConfiguration("", 1, properties);
		
		File testItemsPath = new File(rootPath);
		String[] files = testItemsPath.list();
		
		for (String file : files) {
			in.putItem(rootPath + "/" + file);
		}
	}

	public void setUpExtended(int factor) throws Exception {
		in = new InMemoryHub();
		out = new InMemoryHub();
		in.setComplete();
		
		String rootPath = "testData";
		
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("path", rootPath);

		config = new ModuleConfiguration("", 1, properties);
		
		File testItemsPath = new File(rootPath);
		String[] files = testItemsPath.list();
		
		for (String file : files) {
			for (int i = 0; i < factor; i++) {
				in.putItem(rootPath + "/" + file);
			}
		}
	}
	
	@Test
	public void testProcess() {
		module = new CZSOParserModule(in, out, config, instanceNumber);
		runModule(module);
		
		int itemsCount = 0;
		String item = null;
		while ((item = out.getItem()) != null) {
			assertNotNull(item);
			assertTrue(item.length() > 100);
			itemsCount++;
		}
		
		assertTrue(itemsCount > 0);
		// assertTrue(itemsCount == 86);
	}
	
	@Test
	public void testProcessOriginal() {
		module = new CZSOParserModuleBackup(in, out, config, instanceNumber);
		runModule(module);

		int itemsCount = 0;
		String item = null;
		while ((item = out.getItem()) != null) {
			assertNotNull(item);
			assertTrue(item.length() > 100);
			itemsCount++;
		}
		
		assertTrue(itemsCount > 0);
		// assertTrue(itemsCount == 86);
	}

	@Test
	public void testProcessIterative() {
		long start = 0;
		long finish = 0;
		int runsCount = 1;
		int setupFactor = 100;

		for (int i = 0; i < runsCount; i++) {
			try {
				setUpExtended(setupFactor);
				start = new Date().getTime();
				testProcess();
				finish = new Date().getTime();
				System.out.println("New > " + (finish - start) + "ms");
			} catch (Exception e) {
				e.printStackTrace();
				fail();
			}
		}


		for (int i = 0; i < runsCount; i++) {
			try {
				setUpExtended(setupFactor);
				start = new Date().getTime();
				testProcessOriginal();
				finish = new Date().getTime();
				System.out.println("Original > " + (finish - start) + "ms");
			} catch (Exception e) {
				e.printStackTrace();
				fail();
			}
		}
	}
	
	@Override
	public void testConfiguration() {
	}
	
	private void runModule(Module module) {
		Thread t = new Thread(module);
		t.start();
		
		while (t.isAlive()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail("Interrupted");
			}
		}
	}

}
