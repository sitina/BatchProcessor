package net.sitina.bp.modules;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.impl.InMemoryHub;

import org.junit.Test;

public class FinalModuleTest extends BatchProcessorTestBase {

	@Override
	public void setUp() throws Exception {
	    in = new InMemoryHub();
	    out = new InMemoryHub();

	    in.putItem("123");
	    in.putItem("1234");
	    in.putItem("12345");
	    in.putItem("123456");
	    in.putItem("1234567");
	    in.setComplete();
	    module = new FinalModule(in, out, config, instanceNumber);
	}

	@Override
    @Test
	public void testProcess() {
	    module.run();
	    assertTrue(out.isComplete());
	}

	@Override
	public void testConfiguration() {
	    // nothing important to configure here
	}

}
