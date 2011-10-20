package net.sitina.bp.test.modules;

import static org.mockito.Mockito.verify;
import net.sitina.bp.modules.CZSOParserModule;
import net.sitina.bp.test.BatchProcessorTestBase;

import org.junit.Test;

public class CZSOParserTest extends BatchProcessorTestBase {

	@Override
	public void setUp() throws Exception {
		module = new CZSOParserModule(in, out, config, instanceNumber);
	}

	@Test
	public void testProcess() {
		config.getProperties();
		
		verify(config).getProperties();
		
		assertNotNull(config);
		
		// fail("not implemented yet");
	}

	@Override
	public void testConfiguration() {
		fail("not implemented yet");
	}

}
