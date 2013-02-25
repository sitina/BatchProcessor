/**
 * Copyright (C) 2007-2013, GoodData(R) Corporation. All rights reserved.
 */
package net.sitina.bp.modules;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;
import org.junit.Test;

public class InsolvencyFilesParserModuleTest extends BatchProcessorTestBase {

    private final static String ITEM = "https://isir.justice.cz/isir/ueu/evidence_upadcu_detail.do?id=658b8acb-b7c0-44a5-9369-e3dc75395f90&actSheet=P&pageA=all&pageB=all&pageC=all&pageD=all&pageP=all";

    @Override
    public void setUp() throws Exception {
        in = new InMemoryHub();
        in.setComplete();
        out = new InMemoryHub();
        config = new ModuleConfiguration("InsolvencyFilesParserModule", 1, moduleProperties);
        module = new InsolvencyFilesParserModule(in, out, config, instanceNumber);
    }

    @Override
    public void testProcess() {
        in.putItem(ITEM);
        module.run();

        int count = 0;
        while (!out.isComplete()) {
            String item = out.getItem();
            log.error(item);
            count++;
        }

        assertFalse(count == 0);
        log.error(count);
    }

    @Test
    public void testProcessSecond() {
        in.putItem("https://isir.justice.cz/isir/ueu/evidence_upadcu_detail.do?id=db97147d-6946-4eae-9bf4-56c0e26aa847");
        module.run();

        int count = 0;
        while (!out.isComplete()) {
            String item = out.getItem();
            log.error(item);
            count++;
        }

        assertFalse(count == 0);
        log.error(count);
    }

    @Override
    public void testConfiguration() {
        // no configuration changes to be tested here
    }
}
