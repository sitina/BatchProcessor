package net.sitina.bp.test;

import java.util.Hashtable;

import junit.framework.TestCase;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class BatchProcessorTestBase extends TestCase {

	protected Logger log = Logger.getLogger(this.getClass());
	
	protected Module module;
	
	@Mock
	protected ModuleConfiguration config;
	
	protected Hashtable<String, String> moduleProperties = new Hashtable<String, String>();
	
	@Mock
	protected Hub in;
	
	@Mock
	protected Hub out;
	
	protected int instanceNumber = 0;
	
	@Before
	public abstract void setUp() throws Exception;
	
	@Test
	public abstract void testProcess();	
	
	@Test
	public abstract void testConfiguration();	
	
}
