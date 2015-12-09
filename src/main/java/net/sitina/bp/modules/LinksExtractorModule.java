package net.sitina.bp.modules;

import net.sitina.bp.api.BatchProcessorException;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

import java.util.Collection;
import java.util.HashSet;

public class LinksExtractorModule extends Module {

    protected String sequence = "";

    private int notFoundCount = 0;

    public LinksExtractorModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
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
        log.debug("Getting links from " + item);
        Parser parser = null;

        NodeList list = null;
        try {
            parser = new Parser(item);
            NodeFilter nf = new TagNameFilter("a");
            list = parser.parse(nf);
        } catch (Exception e) {
            try {
                parser.reset();
                NodeFilter nf = new TagNameFilter("a");
                list = parser.parse(nf);
            } catch (Exception e2) {
                throw new BatchProcessorException(this.getClass(), item, e2);
            }
        }

        Collection<String> parseResult = getLinks(list);

        if (parseResult != null && parseResult.size() > 0) {
            for (String link : parseResult) {
                out.putItem(link);
            }
        } else {
            log.debug("No appropriate links found for url " + item);
            notFoundCount++;
            if (notFoundCount == 1000) {
                notFoundCount = 0;
                log.error("No appropriate links found for url " + item);
            }
        }
    }

    private Collection<String> getLinks(NodeList list) {
        Collection<String> result = null;
        SimpleNodeIterator iterator = list.elements();

        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();

            if (node.getClass() == LinkTag.class) {
                LinkTag tag = (LinkTag) node;
                String link = tag.getLink();

                log.debug(link);

                if (link.contains(sequence)) {
                    result = addLink(result, link);
                }
            }
        }

        return result;
    }

    private Collection<String> addLink(Collection<String> links, String link) {
        if (links == null) {
            links = new HashSet<String>();
        }

        links.add(link);

        return links;
    }

}
