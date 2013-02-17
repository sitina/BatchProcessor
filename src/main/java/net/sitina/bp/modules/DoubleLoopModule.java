/**
 * Copyright (C) 2007-2013, GoodData(R) Corporation. All rights reserved.
 */
package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class DoubleLoopModule extends Module {

    private String inputText = "";

    private int startOne = 0;
    private int endOne = 0;
    private int stepOne = 1;
    private int startTwo = 0;
    private int endTwo = 0;
    private int stepTwo = 1;
    private String line;

    public DoubleLoopModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void process(String item) {
        if (inputText != null) {
            item = inputText;
        }

        for (long i = startOne; (stepOne > 0) ? (i <= endOne) : (i >= endOne); i += stepOne) {
            for (long j = startTwo; (stepTwo > 0) ? (j <= endTwo) : (j >= endTwo); j += stepTwo) {
                out.putItem(String.format(inputText, j, i));
            }
        }
    }

    @Override
    protected void loadConfiguration() {
        startOne = configuration.getIntProperty("startOne");
        endOne = configuration.getIntProperty("endOne");
        stepOne = configuration.getIntProperty("stepOne");

        startTwo = configuration.getIntProperty("startTwo");
        endTwo = configuration.getIntProperty("endTwo");
        stepTwo = configuration.getIntProperty("stepTwo");

        inputText = configuration.getStringProperty("inputText");
    }
}