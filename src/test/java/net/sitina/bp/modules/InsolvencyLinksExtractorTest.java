/**
 * Copyright (C) 2007-2013, GoodData(R) Corporation. All rights reserved.
 */
package net.sitina.bp.modules;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.junit.Assert;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.fail;

public class InsolvencyLinksExtractorTest {

    private final static Logger log = Logger.getLogger(InsolvencyLinksExtractorTest.class);

    private final static String SOURCE = "https://isir.justice.cz/isir/ueu/vysledek_lustrace.do?nazev_osoby=&vyhledat_pouze_podle_zacatku=on&podpora_vyhledat_pouze_podle_zacatku=true&jmeno_osoby=&ic=&datum_narozeni=&rc=&mesto=&cislo_senatu=&bc_vec=&rocnik=&id_osoby_puvodce=&druh_stav_konkursu=&datum_stav_od=&datum_stav_do=&aktualnost=AKTUALNI_I_UKONCENA&druh_kod_udalost=&datum_akce_od=&datum_akce_do=&nazev_osoby_f=&rowsAtOnce=400&captcha_answer=&spis_znacky_datum=01.11.2012&spis_znacky_obdobi=MESICNE";

    @Test
    public void testFindLinks() throws Exception {
        try {
            Parser parser = new Parser(SOURCE);

            NodeFilter rowNodesFilter = new HasChildFilter(new HasChildFilter(new LinkRegexFilter(".*evidence_upadcu_detail.do.*")));
            NodeList insolvencyRows = parser.parse(rowNodesFilter);
            listNodes(insolvencyRows);
        } catch (ParserException e) {
            if (!(e.getThrowable() instanceof UnknownHostException)) {
                fail(e.getMessage());
            } else {
                log.error("Test skipped cause of lack of Internet connectivity");
            }
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
            log.error(ico + " " + link);
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
