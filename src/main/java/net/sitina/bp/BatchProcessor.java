package net.sitina.bp;

import net.sitina.bp.core.ModulesRunner;

public class BatchProcessor {

    private static String configFile;

    public static void main(String[] args) {

        // configFile = "config/bpCZSODownload.properties";

        // configFile = "config/bpCZSOProcess.properties";

        // configFile = "config/bpCZSOMergeResults.properties";

        // configFile = "config/bpCZSOSaveResults.properties";

        // configFile = "config/test.properties";

        // configFile = "config/gjwhf.properties";

        // configFile = "/users/Jirka/Documents/workspace/BatchProcessor/config/insolvency.properties";

        // configFile = "/users/Jirka/Documents/workspace/BatchProcessor/config/insolvency_errors.properties";

        // configFile = "/users/Jirka/Documents/workspace/BatchProcessor/config/insolvency_cleanup.properties";

        configFile = "/users/Jirka/Documents/workspace/BatchProcessor/config/mxgp.properties";

        if (args != null && args.length > 0) {
            configFile = args[0];
        }

        ModulesRunner.main(new String[]{configFile});
    }

}
