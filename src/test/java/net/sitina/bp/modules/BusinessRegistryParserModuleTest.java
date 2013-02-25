/**
 * Copyright (C) 2007-2013, GoodData(R) Corporation. All rights reserved.
 */
package net.sitina.bp.modules;

import net.sitina.bp.BatchProcessorTestBase;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.impl.InMemoryHub;
import org.junit.Test;

public class BusinessRegistryParserModuleTest extends BatchProcessorTestBase {

    private static final String INPUT = "'https://isir.justice.cz/isir/ueu/evidence_upadcu_detail.do?id=bb927bab-e0a7-421f-ac83-5fc69f4f6c4a','Václav Dušek','25865366','24.09.2008','Sdělení','https://isir.justice.cz/isir/doc/dokument.PDF?id=166679'";

    private static final String INPUT_NEW = "'https://isir.justice.cz/isir/ueu/evidence_upadcu_detail.do?id=658b8acb-b7c0-44a5-9369-e3dc75395f90&actSheet=P&pageA=all&pageB=all&pageC=all&pageD=all&pageP=all','SUGAL spol. s r.o.','26871548','Prohlášený konkurs','27.10.2010','I_63','Přihláška pohledávky','/isir/doc/dokument.PDF?id=1355165'";

    @Override
    public void setUp() throws Exception {
        in = new InMemoryHub();
        in.setComplete();
        out = new InMemoryHub();
        config = new ModuleConfiguration("BusinessRegistryParserModule", 1, moduleProperties);
        module = new BusinessRegistryParserModule(in, out, config, instanceNumber);
    }

    @Override
    @Test
    public void testProcess() {
        in.putItem(INPUT);
        in.putItem(INPUT_NEW);

        module.run();

        String val = out.getItem();
        assertNotNull(val);
    }

    @Override
    public void testConfiguration() {
        // no configuration in this module
    }

}
