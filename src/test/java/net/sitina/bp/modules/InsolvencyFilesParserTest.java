/**
 * Copyright (C) 2007-2013, GoodData(R) Corporation. All rights reserved.
 */
package net.sitina.bp.modules;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;
import org.junit.Test;

public class InsolvencyFilesParserTest extends BatchProcessorTestBase {

    @Override
    public void setUp() throws Exception {
        in = new InMemoryHub();
        in.setComplete();
        out = new InMemoryHub();
        moduleProperties.put("sequence", "aaa");
        config = new ModuleConfiguration("ICOValidatorModule", 1, moduleProperties);
        module = new InsolvencyFilesParserModule(in, out, config, instanceNumber);
    }

    @Override
    @Test
    public void testProcess() {
        in.putItem("/Users/Jirka/Documents/workspace/BatchProcessor/testData/insolvencyModuleTest.html");

        module.run();

        String val = out.getItem();
        assertNotNull(val);
    }

    @Override
    public void testConfiguration() {
        // no configuration in this module
    }

}