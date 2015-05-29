package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class QuotesReplacerModule extends Module {

    public QuotesReplacerModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void process(String item) {
        item = item.replaceAll("\"", "`");
        item = item.replaceAll("'", "\"");

        out.putItem(item);
    }

    @Override
    protected void loadConfiguration() {
        // no need to configure anything here
    }
}
