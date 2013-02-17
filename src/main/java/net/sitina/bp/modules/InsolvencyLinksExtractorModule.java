/**
 * Copyright (C) 2007-2013, GoodData(R) Corporation. All rights reserved.
 */
package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsolvencyLinksExtractorModule extends Module {

    private final NodeFilter rowNodesFilter = new HasChildFilter(new HasChildFilter(new LinkRegexFilter(".*evidence_upadcu_detail.do.*")));

    private String sequence = "";

    public InsolvencyLinksExtractorModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void loadConfiguration() {
        if (configuration.containsKey("sequence")) {
            sequence = configuration.getStringProperty("sequence");
        }
    }

    @Override
    protected void process(String item) {
        try {
            Parser parser = new Parser(item);
            NodeList insolvencyRows = parser.parse(rowNodesFilter);
            listNodes(insolvencyRows);
        } catch (Exception e) {
            log.error("Problem processing item '" + item + "'.", e);
        }
    }

    private void listNodes(NodeList list) {
        SimpleNodeIterator iterator = list.elements();

        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();
            processInsolvencyRow(node);
        }
    }

    private void processInsolvencyRow(Node documentRow) {
        assert documentRow instanceof TableRow;

        TableRow tableRow = (TableRow)documentRow;
        Node linkNode = tableRow.getChild(15);

        String ico = tableRow.getChild(17).toPlainTextString().trim();
        String link = getLinkTarget(linkNode.toHtml());

        if (ico.length() > 0) {
            String result = sequence + link;
            result = result.replace("&#61;", "=");
            out.putItem(result);
        }
    }

    private String getLinkTarget(String input) {
        input = input.replace("\n", "");
        input = input.replace("\r", "");

        Pattern pattern = Pattern.compile(".*href=\"(.*)\" >.*");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }


}
