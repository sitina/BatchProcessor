package net.sitina.bp.modules;

import net.sitina.bp.api.BatchProcessorException;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * The module reads URI supplied as parameter and returns result as a String
 */
public class StringDownloaderModule extends Module {

    public StringDownloaderModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void process(String item) {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new URL(item).openConnection().getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            throw new BatchProcessorException(this.getClass(), item, e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                }
            }
        }

        out.putItem(sb.toString());
    }

    @Override
    protected void loadConfiguration() {
        // intentionally left blank
    }

}
