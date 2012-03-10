package net.sitina.bp.modules;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;
import net.sitina.bp.modules.FileLoaderModule;
import net.sitina.bp.modules.LinksExtractorModule;

import org.junit.Assert;
import org.junit.Test;

public class LinksExtractorTest extends BatchProcessorTestBase {

	@Override
	public void setUp() throws Exception {
	}

	@Test
	public void testProcess() {
		InMemoryHub flIn = new InMemoryHub();
		InMemoryHub flOut = new InMemoryHub();
		InMemoryHub lpOut = new InMemoryHub();
		
		flIn.putItem("testData/");
		flIn.setComplete();
		
		Module flm = new FileLoaderModule(flIn, flOut, new ModuleConfiguration(getName(), 0, null), 0);
		Module lpm = new LinksExtractorModule(flOut, lpOut, new ModuleConfiguration(getName(), 0, null), 0);
		
		Thread t1 = new Thread(flm);
		t1.start();
		
		Thread t2 = new Thread(lpm);
		t2.start();
		
		while (t2.isAlive()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Assert.assertNotNull(lpOut.getItem());
		
		String lnk = null;
		while ((lnk = lpOut.getItem()) != null) {
			System.out.println(lnk);
		}
	}

	@Override
	public void testConfiguration() {
		// check whether load of "sequence" that should be present
		// in link works
		fail("not implemented yet");
	}

}
