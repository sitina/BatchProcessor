package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class StringReplacerModule extends Module {
    
    protected String oldValue = "";
    protected String newValue = "";

    public StringReplacerModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void loadConfiguration() {
        if (configuration.containsKey("oldValue")) {
            oldValue = configuration.getStringProperty("oldValue");
        }

        if (configuration.containsKey("newValue")) {
            newValue = configuration.getStringProperty("newValue");
        }
    }

    @Override
    protected void process(String item) {
        out.putItem(item.replace(oldValue, newValue));
    }

}
