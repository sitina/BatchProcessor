package net.sitina.bp.modules;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;

import org.junit.After;
import org.junit.Test;

public class FileAppenderTest extends BatchProcessorTestBase {

    private final String path = "/tmp/file";

    @Override
    public void setUp() throws Exception {
        in = new InMemoryHub();
        out = new InMemoryHub();

        in.putItem("value");
        in.setComplete();

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("path", path);

        config = new ModuleConfiguration(getClass().toString(), 1, properties);

        module = new FileAppenderModule(in, out, config, instanceNumber);
    }

    @After
    public void cleanup() throws Exception {
        File result = new File(path);
        if (result.exists()) {
            result.delete();
        }
    }

    @Override
    @Test
    public void testProcess() {
        File result = new File(path);
        assertTrue(!result.exists());

        module.run();

        assertTrue(result.exists());
    }

    @Test
    public void testProcessExtension() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("path", path);
        properties.put("extension", "tmp");

        config = new ModuleConfiguration(getClass().toString(), 1, properties);

        module = new FileAppenderModule(in, out, config, instanceNumber);

        File result = new File(path + ".tmp");
        assertTrue(!result.exists());

        module.run();

        assertTrue(result.exists());
        result.delete();
    }

    @Override
    public void testConfiguration() {
        // nothing special to test here
    }

}
