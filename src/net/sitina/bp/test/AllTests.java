package net.sitina.bp.test;

import net.sitina.bp.test.db.DerbyTest;
import net.sitina.bp.test.modules.CZSOExtractorTest;
import net.sitina.bp.test.modules.CZSOParserTest;
import net.sitina.bp.test.modules.FileAppenderTest;
import net.sitina.bp.test.modules.FileDownloaderTest;
import net.sitina.bp.test.modules.FileLoaderTest;
import net.sitina.bp.test.modules.FileStringReaderTest;
import net.sitina.bp.test.modules.FinalModuleTest;
import net.sitina.bp.test.modules.ICOValidatorTest;
import net.sitina.bp.test.modules.ImagesExtractorTest;
import net.sitina.bp.test.modules.LinksExtractorTest;
import net.sitina.bp.test.modules.LoopModuleTest;
import net.sitina.bp.test.modules.StringAppenderTest;
import net.sitina.bp.test.modules.StringDownloaderTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for net.sitina.bp.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(PropertiesConfigurationTest.class);
		
		// db
		suite.addTestSuite(DerbyTest.class);
		
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
