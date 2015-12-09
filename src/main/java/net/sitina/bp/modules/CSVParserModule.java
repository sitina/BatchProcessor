package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;
import net.sitina.bp.api.ModuleParameter;
import net.sitina.bp.util.BPStringTokenizer;

public class CSVParserModule extends Module {

    private static final String PREFFERED_FIELD_PROPERTY = "prefferedField";

    private static final String DELIMITER_PROPERTY = "delimiter";

    @ModuleParameter(description = "Delimiter of items", required = false)
    private String delimiter = ";";

    @ModuleParameter(description = "Which field should be returned", required = false)
    private int prefferedField = 0;

    public CSVParserModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void process(String item) {
        //TODO: rewrite the logic

        BPStringTokenizer strTok = new BPStringTokenizer(item, delimiter);

        for (int i = 0; i < prefferedField; i++) {
            strTok.nextToken();
        }

        out.putItem(strTok.nextToken());
    }

    @Override
    protected void loadConfiguration() {
        if (configuration.containsKey(DELIMITER_PROPERTY)) {
            delimiter = configuration.getStringProperty(DELIMITER_PROPERTY);
        }

        if (configuration.containsKey(PREFFERED_FIELD_PROPERTY)) {
            prefferedField = configuration.getIntProperty(PREFFERED_FIELD_PROPERTY);
        }
    }

}
