package net.sitina.bp.modules;

import java.util.Collection;
import java.util.HashSet;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class LinksExtractorModule extends Module {
	
	protected String sequence = "";

	protected Integer limit;
	
	private final Collection<String> links = new HashSet<String>();

	public LinksExtractorModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void loadConfiguration() {
		if (configuration.containsKey("sequence")) {
			sequence =  configuration.getStringProperty("sequence");
		}
		
		if (configuration.containsKey("limit")) {
			limit = configuration.getIntProperty("limit");
		}
	}

	@Override
	protected void process(String item) {
		log.debug("Getting links from " + item);
		Parser parser = null;
		
		NodeList list = null;
		try {
			parser = new Parser(item);
			list = parser.parse(null);
		} catch (Exception e) {
			try {
				parser.reset();
				list = parser.parse(null);
				log.debug(list.size());
			} catch (Exception e2) {
				e.printStackTrace();
			}
		}

		getLinks(list);

		for (String link : links) {
			out.putItem(link);
		}
	}

	private void getLinks(NodeList list) {
		SimpleNodeIterator iterator = list.elements();
		
		while (iterator.hasMoreNodes()) {
			Node node = iterator.nextNode();

			if (node.getClass() == LinkTag.class) {
				LinkTag tag = (LinkTag) node;
				String link = tag.getLink();

				log.debug(link);
				
				if (link.contains(sequence)) {
					links.add(link);
				}

			} else if (node.getChildren() != null) {
				getLinks(node.getChildren());
			}
		}
	}

}
