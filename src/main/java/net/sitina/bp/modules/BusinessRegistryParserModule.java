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
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

import java.util.HashMap;
import java.util.Map;

public class BusinessRegistryParserModule extends Module {

    private final NodeFilter companyDetailsFilter = new LinkRegexFilter(".*/ui/vypis-vypis.*");
    private final static Map<String, String> CACHE = new HashMap<String, String>();
    private static Long HITS = 0L;
    private static Long MISSES = 0L;

    public BusinessRegistryParserModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void loadConfiguration() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void process(String item) {
        try {
            // expected value is the result of {@link InsolvencyFilesParserModule}

            // fetch business ID - text between second and third comma
            String businessID = getBusinessID(item);

            if (CACHE.containsKey(businessID)) {
                HITS++;
                out.putItem(item + CACHE.get(businessID));
                return;
            }

            // go to the business registry ==> https://or.justice.cz/ias/ui/rejstrik-firma.vysledky?nazev=&ic=25865366&polozek=50&typHledani=prefix&jenPlatne=false
            String businessSearchUrl = "https://or.justice.cz/ias/ui/rejstrik-firma.vysledky?nazev=&polozek=50&typHledani=prefix&jenPlatne=false&ic=" + businessID;

            // get link of detail result ==> https://or.justice.cz/ias/ui/vypis-vypis?subjektId=isor%3a434258&typ=full&klic=fei5dc
            Parser parser = new Parser(businessSearchUrl);
            NodeList detailLinkNodes = parser.parse(companyDetailsFilter);
            String detailLink = getLink(detailLinkNodes);

            // fetch result + append info to the 'item' property
            if (detailLink != null) {
                detailLink = detailLink.replaceAll("&amp;","&");
                Parser detailParser = new Parser(detailLink);

                String enrollmentDate = getRegistryEnrollmentDate(detailParser);
                detailParser.reset();

                String businessType = getBusinessType(detailParser);
                businessType = (businessType != null) ? businessType : "-";
                detailParser.reset();

                String basicProperty = getBasicProperty(detailParser);
                basicProperty = (basicProperty != null) ? basicProperty : "-";
                detailParser.reset();

                StringBuilder result = new StringBuilder(",'");
                result.append(enrollmentDate);
                result.append("','");
                result.append(businessType);
                result.append("','");
                result.append(basicProperty);
                result.append("'");

                if (CACHE.size() > 1000) {
                    log.debug(HITS + "/" + MISSES);
                    CACHE.clear();
                }
                CACHE.put(businessID, result.toString());
                MISSES++;

                item += result.toString();
            } else {
                item += ",'-','-','-'";
            }
        } catch (Exception e) {
            log.error("Problem fetching details for item " + item, e);
        }
        out.putItem(item);
    }

    private String getRegistryEnrollmentDate(Parser detailParser) throws Exception {
        NodeFilter enrollmentFilter = new AndFilter(new HasParentFilter(new HasAttributeFilter("class", "vypis-row vypis-datumZapisu")), new NodeClassFilter(TableColumn.class));
        NodeList businessFormNodes = detailParser.parse(enrollmentFilter);
        SimpleNodeIterator iterator = businessFormNodes.elements();

        int counter = 0;
        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();
            if (counter == 0) {
                String result = node.toPlainTextString().replaceAll("\n", "").replaceAll("\r","").trim();
                result = result.replace("ledna", "1.");
                result = result.replace("února", "2.");
                result = result.replace("března", "3.");
                result = result.replace("dubna", "4.");
                result = result.replace("května", "5.");
                result = result.replace("června", "6.");
                result = result.replace("července", "7.");
                result = result.replace("srpna", "8.");
                result = result.replace("září", "9.");
                result = result.replace("října", "10.");
                result = result.replace("listopadu", "11.");
                result = result.replace("prosince", "12.");
                result = result.replaceAll(" ", "");
                return result;
            }
            counter++;
        }

        return null;
    }

    private String getBusinessType(Parser detailParser) throws Exception {
        NodeFilter businessFormFilter = new HasParentFilter(new HasParentFilter(new HasAttributeFilter("class", "vypis-row vypis-udaj-single vypis-udaj-pravniForma ")));
        NodeList businessFormNodes = detailParser.parse(businessFormFilter);
        SimpleNodeIterator iterator = businessFormNodes.elements();

        int counter = 0;
        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();
            if (counter == 3) {
                return node.toPlainTextString().replaceAll("\n", "").replaceAll("\r", "").trim();
            }
            counter++;
        }

        return null;
    }

    private String getBasicProperty(Parser detailParser) throws Exception {
        NodeFilter basicPropertyFilter = new HasParentFilter(new HasParentFilter(new HasAttributeFilter("class", "vypis-row vypis-udaj-single vypis-udaj-zakladniKapital ")));
        NodeList basicPropertyNodes = detailParser.parse(basicPropertyFilter);
        SimpleNodeIterator iterator = basicPropertyNodes.elements();

        int counter = 0;
        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();
            if (counter == 3) {
                String result =  node.toPlainTextString().replaceAll("\n", "").replaceAll("\r","").trim();
                result = result.replace(",-", "");
                result = result.replace("Kč", "");
                result = result.replaceAll("\\s", "");
                result = result.replace(String.valueOf(Character.toChars(160)), "");
                return result;
            }
            counter++;
        }

        return null;
    }

    private String getLink(NodeList list) {
        SimpleNodeIterator iterator = list.elements();

        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();
            if (node instanceof LinkTag) {
                LinkTag link = (LinkTag)node;
                return link.getLink();
            }
        }

        return null;
    }


    private String getBusinessID(String item) {
        return item.substring(nthOccurrence(item, ',', 1) + 2, nthOccurrence(item, ',', 2) - 1);
    }

    public static int nthOccurrence(String str, char c, int n) {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1)
            pos = str.indexOf(c, pos+1);
        return pos;
    }

}
