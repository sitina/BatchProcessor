/**
 * Copyright (C) 2007-2013, GoodData(R) Corporation. All rights reserved.
 */
package net.sitina.bp.modules;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;

public class QuotesReplacerModuleTest extends BatchProcessorTestBase {
    @Override
    public void setUp() throws Exception {
        in = new InMemoryHub();
        in.setComplete();
        out = new InMemoryHub();
        config = new ModuleConfiguration("QuotesReplacerModule", 1, moduleProperties);
        module = new QuotesReplacerModule(in, out, config, instanceNumber);
    }

    @Override
    public void testProcess() {
        in.putItem("'item with \"quotes\"','field','something else'");

        module.run();

        String result = out.getItem();
        assertNotNull(result);
        assertEquals("\"item with `quotes`\",\"field\",\"something else\"", result);
    }

    @Override
    public void testConfiguration() {
        // nope
    }
}
