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
import org.htmlparser.filters.*;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsolvencyFilesParserModule extends Module {

    private String sequence = "";

    public InsolvencyFilesParserModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
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

            NodeFilter nameFilter = new AndFilter(new HasSiblingFilter(new HasChildFilter(new RegexFilter(".*Jméno/název:.*"))), new NodeClassFilter(TableColumn.class));
            NodeList nameNodes = parser.parse(nameFilter);
            String name = getName(nameNodes);
            parser.reset();

            NodeFilter icoFilter = new AndFilter(new AndFilter(new HasSiblingFilter(new HasChildFilter(new RegexFilter(".*IČ:.*"))), new NodeClassFilter(TableColumn.class)), new HasChildFilter(new NodeClassFilter(LinkTag.class)));
            NodeList icoNodes = parser.parse(icoFilter);
            String ico = null;
            if (icoNodes.size() > 0) {
                ico = getICO(icoNodes.elementAt(0).toHtml());
            }
            parser.reset();

            NodeFilter documentsFilter = new AndFilter(new HasChildFilter(new HasChildFilter(new LinkRegexFilter("/isir/doc/.*"))), new NodeClassFilter(TableRow.class));
            NodeList documentsList = parser.parse(documentsFilter);
            listDocuments(item, name, ico, documentsList);
            parser.reset();
        } catch (Exception e) {
            log.error("Problem processing item '" + item + "'.", e);
        }
    }

    private String getName(NodeList list) {
        SimpleNodeIterator iterator = list.elements();

        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();

            String nodeText = node.toPlainTextString();
            if (nodeText.trim().length() > 0 && !nodeText.trim().equals("Jméno/název:")) {
                return nodeText.trim();
            }
        }

        return null;
    }

    private void listDocuments(String link, String name, String ico, NodeList list) {
        SimpleNodeIterator iterator = list.elements();

        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();
            listDocumentRow(link, name, ico, node);
        }
    }

    private void listDocumentRow(String link, String name, String ico, Node documentRow) {
        assert documentRow instanceof TableRow;

        TableRow tableRow = (TableRow)documentRow;

        Node date = tableRow.getChild(3);
        Node documentType = tableRow.getChild(7);
        Node documentLink = tableRow.getChild(11);

        String dateString = stripSpan(date.toHtml());
        String documentTypeString = stripSpan(documentType.toHtml());
        documentTypeString = documentTypeString.replaceAll("\\s+", " ");
        documentTypeString = documentTypeString.replaceAll("</span>", "");
        documentTypeString = documentTypeString.replaceAll("<br >", "");
        documentTypeString = documentTypeString.replaceAll("<span class='' >", "");
        String documentLinkString = sequence + getLinkTarget(documentLink.toHtml());

//        log.error("link: '" + link + "'");
//        log.error("name: '" + name + "'");
//        log.error("ico: '" + ico + "'");
//        log.error("dateString: '" + dateString + "'");
//        log.error("documentType: '" + documentTypeString + "'");
//        log.error("documentLink: '" + documentLinkString + "'");

        StringBuilder result = new StringBuilder("'");
        result.append(link);
        result.append("','");
        result.append(name.replace(",", "."));
        result.append("','");
        result.append(ico);
        result.append("','");
        result.append(dateString);
        result.append("','");
        result.append(documentTypeString.replace(",", "."));
        result.append("','");
        result.append(documentLinkString);
        result.append("'");

        out.putItem(result.toString());
    }

    private String stripSpan(String input) {
        input = input.replace("\n", "");
        input = input.replace("\r", "");
        Pattern pattern = Pattern.compile(".*<span class=''>(.*)</span>.*");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }

    private String getLinkTarget(String input) {
        input = input.replace("\n", "");
        input = input.replace("\r", "");

        Pattern pattern = Pattern.compile(".*href=\"(.*)\" target.*");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }

    private String getICO(String input) {
        input = input.replace("\n", "");
        input = input.replace("\r", "");
        input = input.replace(" ", "");

        Pattern pattern = Pattern.compile("<td>([0-9]*).*");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }

}
