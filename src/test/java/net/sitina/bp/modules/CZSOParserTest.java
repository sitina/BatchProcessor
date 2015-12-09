package net.sitina.bp.modules;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;

import org.junit.Test;

public class CZSOParserTest extends BatchProcessorTestBase {

    @Override
    public void setUp() throws Exception {
        in = new InMemoryHub();
        out = new InMemoryHub();
        in.setComplete();

        String rootPath = "testData";

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("path", rootPath);

        config = new ModuleConfiguration("", 1, properties);

        File testItemsPath = new File(rootPath);
        String[] files = testItemsPath.list();

        for (String file : files) {
            in.putItem(rootPath + "/" + file);
        }
    }

    public void setUpExtended(int factor) throws Exception {
        in = new InMemoryHub();
        out = new InMemoryHub();
        in.setComplete();

        String rootPath = "testData";

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("path", rootPath);

        config = new ModuleConfiguration("", 1, properties);

        File testItemsPath = new File(rootPath);
        String[] files = testItemsPath.list();

        for (String file : files) {
            for (int i = 0; i < factor; i++) {
                in.putItem(rootPath + "/" + file);
            }
        }
    }

    @Override
    @Test
    public void testProcess() {
        module = new CZSOParserModule(in, out, config, instanceNumber);
        runModule(module);

        int itemsCount = 0;
        String item = null;
        while ((item = out.getItem()) != null) {
            assertNotNull(item);
            assertTrue(item.length() > 100);
            itemsCount++;
        }

        assertTrue(itemsCount > 0);
        // assertTrue(itemsCount == 86);
    }

    @Test
    public void testProcess2() {
        module = new CZSOParserModule(in, out, config, instanceNumber);
        runModule(module);

        int itemsCount = 0;
        String item = null;
        while ((item = out.getItem()) != null) {
            assertNotNull(item);
            assertTrue(item.length() > 100);
            itemsCount++;
        }

        assertTrue(itemsCount > 0);
        assertTrue(itemsCount == 86);
    }

    @Override
    public void testConfiguration() {
    }

    private void runModule(Module module) {
        Thread t = new Thread(module);
        t.start();

        while (t.isAlive()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail("Interrupted");
            }
        }
    }

}
