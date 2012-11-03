package net.sitina.bp.modules;

import org.junit.Test;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;

public class FileStringReaderTest extends BatchProcessorTestBase {

	@Override
	public void setUp() throws Exception {
        in = new InMemoryHub();
        in.setComplete();
        out = new InMemoryHub();
        config = new ModuleConfiguration("FileStringReaderModule", 1, moduleProperties);
        module = new FileStringReaderModule(in, out, config, instanceNumber);
	}

	@Test
	public void testProcess() {
        in.putItem("config/test.properties");

        module.run();

        String val = out.getItem();

        assertNotNull(val);
        assertEquals("bp.modules.1.class=net.sitina.bp.modules.LoopModule", val);
	}

	@Override
	public void testConfiguration() {
	    // kept empty..
	}

}
