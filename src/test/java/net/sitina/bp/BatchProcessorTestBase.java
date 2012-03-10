package net.sitina.bp;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public abstract class BatchProcessorTestBase extends TestCase {

	protected Logger log = Logger.getLogger(this.getClass());

	protected Module module = Mockito.mock(Module.class);

	protected ModuleConfiguration config = Mockito.mock(ModuleConfiguration.class);

	protected Map<String, String> moduleProperties = new HashMap<String, String>();

	protected Hub in = Mockito.mock(Hub.class);

	protected Hub out = Mockito.mock(Hub.class);

	protected int instanceNumber = 0;

	@Override
    @Before
	public abstract void setUp() throws Exception;

	@Test
	public abstract void testProcess();

	@Test
	public abstract void testConfiguration();

}
