package net.sitina.bp.test.modules;

import java.util.Hashtable;

import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;
import net.sitina.bp.modules.LoopModule;
import net.sitina.bp.test.BatchProcessorTestBase;

import org.junit.Test;

public class LoopModuleTest extends BatchProcessorTestBase {

	@Override
	public void setUp() throws Exception {
		in = new InMemoryHub();
		in.putItem("");
		in.setComplete();
		out = new InMemoryHub();
		moduleProperties.put("start", "0");
		moduleProperties.put("end", "1");
		moduleProperties.put("step", "1");
		moduleProperties.put("inputText", "%s");
		config = new ModuleConfiguration("LoopModule", 1, moduleProperties);
		module = new LoopModule(in, out, config, instanceNumber);
	}

	@Test
	public void testProcess() {
		module.run();
		
		assertEquals("0", out.getItem());
		assertEquals("1", out.getItem());
		assertNull(out.getItem());
	}

	@Test
	public void testInvalidBounds() {
		moduleProperties = new Hashtable<String, String>();
		
		moduleProperties.put("start", "1");
		moduleProperties.put("end", "0");
		moduleProperties.put("step", "1");
		moduleProperties.put("inputText", "%s");
		
		config = new ModuleConfiguration("LoopModule", 1, moduleProperties);
		module = new LoopModule(in, out, config, instanceNumber);

		module.run();
		
		assertNull(out.getItem());
	}

	@Test
	public void testIncludeBounds() {
		moduleProperties = new Hashtable<String, String>();
		
		moduleProperties.put("start", "0");
		moduleProperties.put("end", "1000");
		moduleProperties.put("step", "1");
		moduleProperties.put("inputText", "%s");
		
		config = new ModuleConfiguration("LoopModule", 1, moduleProperties);
		module = new LoopModule(in, out, config, instanceNumber);

		module.run();
		
		for (int i = 0; i < 1001; i++) {
			String val = out.getItem();
			assertNotNull(val);
			assertEquals("" + i, val);
		}
		
		assertNull(out.getItem());
	}

	@Override
	public void testConfiguration() {
		assertEquals("%s", config.getStringProperty("inputText"));
		assertEquals(0, config.getIntProperty("start"));
	}

}
