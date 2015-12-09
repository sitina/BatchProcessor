package net.sitina.bp.modules;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;
import org.junit.Test;

public class DateParserModuleTest extends BatchProcessorTestBase {

    @Override
    public void setUp() throws Exception {
        in = new InMemoryHub();
        in.setComplete();
        out = new InMemoryHub();
        moduleProperties.put("positions", "1");
        config = new ModuleConfiguration("DateParserModule", 1, moduleProperties);
        module = new DateParserModule(in, out, config, instanceNumber);
    }

    @Override
    public void testProcess() {
        in.putItem("row,'1.2.2013',somethingElse");
        in.putItem("ssss,'12.5.2002'");

        module.run();

        String result1 = out.getItem();
        assertNotNull(result1);
        assertEquals("row,2013-02-01,somethingElse", result1);

        String result2 = out.getItem();
        assertNotNull(result2);
        assertEquals("ssss,2002-05-12", result2);
    }

    @Override
    public void testConfiguration() {
        in = new InMemoryHub();
        in.setComplete();
        out = new InMemoryHub();
        moduleProperties.put("positions", "1");
        config = new ModuleConfiguration("DateParserModule", 1, moduleProperties);
        module = new DateParserModule(in, out, config, instanceNumber);
    }

    @Test
    public void processCorrectDate() {
        String date = ((DateParserModule) module).processDate("'12.5.2035'");
        assertNotNull(date);
        assertEquals("'2035-05-12'", date);
    }

}
