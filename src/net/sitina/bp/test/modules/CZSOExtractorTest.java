package net.sitina.bp.test.modules;

import net.sitina.bp.impl.InMemoryHub;
import net.sitina.bp.modules.CZSOParserModule;
import net.sitina.bp.test.BatchProcessorTestBase;

public class CZSOExtractorTest extends BatchProcessorTestBase {

	@Override
	public void setUp() throws Exception {
		in = new InMemoryHub();
		in.putItem("test1");
		in.setComplete();
		
		out = new InMemoryHub();
		
		module = new CZSOParserModule(in, out, config, instanceNumber);
	}

	@Override
	public void testProcess() {
		module.run();
		
		assertTrue(in.isComplete());
		assertNotNull(out.getItem());
	}

	@Override
	public void testConfiguration() {
		fail("not implemented yet");
	}
	
}