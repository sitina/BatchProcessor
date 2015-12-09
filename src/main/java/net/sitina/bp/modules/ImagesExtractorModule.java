package net.sitina.bp.modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

import net.sitina.bp.api.BatchProcessorException;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

public class ImagesExtractorModule extends Module {

    private final Collection<String> images = new HashSet<String>();

    private final List<String> extensions = new ArrayList<String>();

    public ImagesExtractorModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void process(String item) {
        log.debug("images: " + item);
        images.clear();

        Parser parser = null;
        NodeList list = null;
        try {
            parser = new Parser(item);
            list = parser.parse(null);
        } catch (Exception e) {
            try {
                parser.reset();
                list = parser.parse(null);
            } catch (Exception e2) {
                throw new BatchProcessorException(this.getClass(), item, e2);
            }
        }

        if (list != null) {
            getImages(list);
        }

        for (String image : images) {
            out.putItem(image);
        }
    }

    private void getImages(NodeList list) {
        SimpleNodeIterator iterator = list.elements();

        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();

            if (node.getClass() == LinkTag.class) {

            }

            if (node.getClass() == ImageTag.class) {
                ImageTag tag = (ImageTag) node;
                String imageUrl = tag.getImageURL();
                for (String extension : extensions) {
                    if (imageUrl.endsWith(extension)) {
                        images.add(imageUrl);
                        break;
                    }
                }
            } else if (node.getChildren() != null) {
                getImages(node.getChildren());
            }
        }
    }

    @Override
    protected void loadConfiguration() {
        if (configuration.containsKey("extensions")) {
            StringTokenizer extensionsTokeniser = new StringTokenizer(configuration.getStringProperty("extensions"), ",");

            while (extensionsTokeniser.hasMoreTokens()) {
                extensions.add(extensionsTokeniser.nextToken());
            }
        }
    }

}
