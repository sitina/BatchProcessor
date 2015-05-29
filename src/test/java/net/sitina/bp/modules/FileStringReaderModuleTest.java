package net.sitina.bp.modules;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;

import java.io.File;
import java.io.FileWriter;

public class FileStringReaderModuleTest extends BatchProcessorTestBase {
    @Override
    public void setUp() throws Exception {
        in = new InMemoryHub();
        in.setComplete();
        out = new InMemoryHub();
        config = new ModuleConfiguration("FileStringReaderModule", 1, moduleProperties);
        module = new FileStringReaderModule(in, out, config, instanceNumber);
    }

    @Override
    public void testProcess() {
        try {
            String fileName = "/tmp/aaa";
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file);
            fw.append("aaaaa\n");
            fw.append("bbbbb\n");
            fw.append("ccccc\n");
            fw.close();

            in.putItem(fileName);

            module.run();

            String res1 = out.getItem();
            String res2 = out.getItem();
            String res3 = out.getItem();
            assertNotNull(res1);
            assertNotNull(res2);
            assertNotNull(res3);
            assertEquals("aaaaa", res1);
            assertEquals("bbbbb", res2);
            assertEquals("ccccc", res3);

            file.delete();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Override
    public void testConfiguration() {
    }
}
