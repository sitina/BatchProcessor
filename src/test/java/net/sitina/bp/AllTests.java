package net.sitina.bp;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sitina.bp.modules.CZSOExtractorTest;
import net.sitina.bp.modules.CZSOParserTest;
import net.sitina.bp.modules.FileAppenderTest;
import net.sitina.bp.modules.FileDownloaderTest;
import net.sitina.bp.modules.FileLoaderTest;
import net.sitina.bp.modules.FileStringReaderTest;
import net.sitina.bp.modules.FinalModuleTest;
import net.sitina.bp.modules.ICOValidatorTest;
import net.sitina.bp.modules.ImagesExtractorTest;
import net.sitina.bp.modules.LinksExtractorTest;
import net.sitina.bp.modules.LoopModuleTest;
import net.sitina.bp.modules.StringAppenderTest;
import net.sitina.bp.modules.StringDownloaderTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for net.sitina.bp.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(PropertiesConfigurationTest.class);
		
		// modules
		suite.addTestSuite(CZSOExtractorTest.class);
		suite.addTestSuite(CZSOParserTest.class);
		suite.addTestSuite(FileAppenderTest.class);
		suite.addTestSuite(FileDownloaderTest.class);
		suite.addTestSuite(FileLoaderTest.class);
		suite.addTestSuite(FileStringReaderTest.class);
		suite.addTestSuite(FinalModuleTest.class);
		suite.addTestSuite(ICOValidatorTest.class);
		suite.addTestSuite(ImagesExtractorTest.class);
		suite.addTestSuite(LinksExtractorTest.class);
		suite.addTestSuite(LoopModuleTest.class);
		suite.addTestSuite(StringAppenderTest.class);
		suite.addTestSuite(StringDownloaderTest.class);		
		
		//$JUnit-END$
		return suite;
	}

}
