package net.sitina.bp.modules;

import java.io.File;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class FileLoaderModule extends Module {

    protected String path = "";

    public FileLoaderModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void process(String item) {
        if (path == null || path.equals("")) {
            path = item;
        }

        File f = new File(path);
        String[] filesList = f.list();

        for (String file : filesList) {
            File ff = new File(path + "/" + file);
            if (ff.isFile()) {
                out.putItem(path + "/" + file);
            }
        }
    }

    @Override
    protected void loadConfiguration() {
        if (configuration.containsKey("path")) {
            path = configuration.getStringProperty("path");
        }
    }

}
