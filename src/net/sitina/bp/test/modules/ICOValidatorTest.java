package net.sitina.bp.test.modules;

import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;
import net.sitina.bp.modules.ICOValidatorModule;
import net.sitina.bp.test.BatchProcessorTestBase;

import org.junit.Test;

public class ICOValidatorTest extends BatchProcessorTestBase {

	@Override
	public void setUp() throws Exception {
		in = new InMemoryHub();
		in.setComplete();
		out = new InMemoryHub();
		config = new ModuleConfiguration("ICOValidatorModule", 1, moduleProperties);
		module = new ICOValidatorModule(in, out, config, instanceNumber);
	}

	@Test
	public void testProcess() {
		in.putItem("3");
		in.putItem("44392851");
		in.putItem("11");
		in.putItem("2");
		
		module.run();
		
		String val = out.getItem();
		
		assertNotNull(val);
		assertEquals("44392851", val);
		
		assertTrue(out.isComplete());
	}

	@Override
	public void testConfiguration() {
		fail("not implemented yet");
	}

}
