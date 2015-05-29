/**
 * Copyright (C) 2007-2013, GoodData(R) Corporation. All rights reserved.
 */
package net.sitina.bp.modules;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.junit.Assert;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class InsolvencyParserTest {

    private final static Logger log = Logger.getLogger(InsolvencyParserTest.class);

    private final static String SOURCE = "https://isir.justice.cz/isir/ueu/evidence_upadcu_detail.do?id=69785fbb-9c44-4d5d-b67a-d9d517fb329b#Henniges";

    @Test
    public void testProcessDocument() throws Exception {
        try {
            Parser parser = new Parser(SOURCE);

            NodeFilter nameFilter = new AndFilter(new HasSiblingFilter(new HasChildFilter(new RegexFilter(".*Jméno/název:.*"))), new NodeClassFilter(TableColumn.class));
            NodeList nameNodes = parser.parse(nameFilter);
            String name = getName(nameNodes);
            log.error("name: " + name);
            parser.reset();

            log.error("*****************");

            NodeFilter icoFilter = new AndFilter(new AndFilter(new HasSiblingFilter(new HasChildFilter(new RegexFilter(".*IČ:.*"))), new NodeClassFilter(TableColumn.class)), new HasChildFilter(new NodeClassFilter(LinkTag.class)));
            NodeList icoNodes = parser.parse(icoFilter);
            String icoHtml = icoNodes.elementAt(0).toHtml();
            String ico = getICO(icoHtml);
            String businessRegistryLink = getBusinessRegistryLink(icoHtml);
            assertNotNull(businessRegistryLink);
            log.error("ico: " + ico);
            parser.reset();

            NodeFilter documentsFilter = new AndFilter(new HasChildFilter(new HasChildFilter(new LinkRegexFilter("/isir/doc/.*"))), new NodeClassFilter(TableRow.class));
            NodeList documentsList = parser.parse(documentsFilter);
            listDocuments(documentsList);
            parser.reset();
        } catch (ParserException e) {
            if (!(e.getThrowable() instanceof UnknownHostException)) {
                fail(e.getMessage());
            } else {
                log.error("Test skipped cause of lack of Internet connectivity");
            }
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

    private void listDocuments(NodeList list) {
        SimpleNodeIterator iterator = list.elements();

        while (iterator.hasMoreNodes()) {
            log.error("*****************");
            Node node = iterator.nextNode();
            listDocumentRow(node);
        }
    }

    private void listDocumentRow(Node documentRow) {
        assert documentRow instanceof TableRow;

        TableRow tableRow = (TableRow)documentRow;

        Node date = tableRow.getChild(3);
        Node documentType = tableRow.getChild(7);
        Node documentLink = tableRow.getChild(11);

        String dateString = stripSpan(date.toHtml());
        String documentTypeString = stripSpan(documentType.toHtml());
        String documentLinkString = getLinkTarget(documentLink.toHtml());

        log.error("dateString: '" + dateString + "'");
        log.error("documentType: '" + documentTypeString + "'");
        log.error("documentLink: '" + documentLinkString + "'");
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

    private String getBusinessRegistryLink(String input) {
        // https://or.justice.cz/ias/ui/rejstrik-firma.vysledky?nazev=&ic=
        return null;
    }


    @Test
    public void testStripSpan() {
        String input = "<TD ALIGN=\"center\">\n" +
                 "              <span class=''>\n" +
                 "                01.12.2008\n" +
                 "              </span>\n" +
                 "            </TD>";

        String result = stripSpan(input);
        Assert.assertEquals("01.12.2008", result);
    }

    @Test
    public void testGetLinkTarget() {
        String input = "<TD>\n" +
                "              <CENTER>\n" +
                "                \n" +
                "                     <a title=\"Otevřít hlavní dokument k události v novém okně jako PDF\" class='' href=\"/isir/doc/dokument.PDF?id=214813\" target=\"_blank\" >\n" +
                "                       plný text\n" +
                "                       (135834 kB)\n" +
                "                     </a>\n" +
                "                \n" +
                "              </CENTER>\n" +
                "            </TD>";

        String result = getLinkTarget(input);
        Assert.assertEquals("/isir/doc/dokument.PDF?id=214813", result);
    }

    @Test
    public void testGetICO() {
        String input = "\n" +
                "<td>\n" +
                "        48393223\n" +
                "        \n" +
                "            &nbsp;&nbsp;\n" +
                "            <a href=\"https://or.justice.cz/ias/ui/rejstrik-firma.vysledky?nazev=&amp;ic=48393223&amp;polozek=50&amp;typHledani=prefix&amp;jenPlatne=true\">\n" +
                "              (viz obchodní rejstřík)\n" +
                "            </a>  \n" +
                "        \n" +
                "      </td>";

        String result = getICO(input);
        Assert.assertEquals("48393223", result);
    }

}
