package net.sitina.bp.modules;

import net.sitina.bp.api.BatchProcessorException;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;

public class TestStringModule extends Module {

    public TestStringModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void process(String item) {
        out.putItem(item);
        log.debug("testovaci chyba, zpracovavam " + item);
        throw new BatchProcessorException(this.getClass(), item, new RuntimeException());
    }

    @Override
    protected void loadConfiguration() {
    }

}
