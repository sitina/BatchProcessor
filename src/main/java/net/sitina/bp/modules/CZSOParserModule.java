package net.sitina.bp.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sitina.bp.api.BatchProcessorException;
import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.util.NodeList;

public class CZSOParserModule extends Module {

	protected List<String> cols = new ArrayList<String>();

	private String path = "";
	
	public CZSOParserModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		super(in, out, config, instanceNumber);
		loadConfiguration();
	}

	@Override
	protected void loadConfiguration() {
		if (configuration.containsKey("path")) {
			path = configuration.getStringProperty("path");
		}
	}

	@Override
	protected void process(String item) {
		try {
			parse(item);
		} catch (Exception e) {
			throw new BatchProcessorException(this.getClass(), item, e);
		}
		
		Map<String, String> table = new HashMap<String, String>();
		
		for (int i = 0; i < cols.size(); i++) {
			if (i + 2 < cols.size() && (
					cols.get(i).contains("IČO") ||
					cols.get(i).contains("firma") ||
					cols.get(i).contains("forma:") ||
					cols.get(i).contains("Datum") ||
					cols.get(i).contains("Velikostn") ||
					cols.get(i).contains("dle CZ") ||
					cols.get(i).contains("sektor") ||
					cols.get(i).contains("Okres")
					)) {
				table.put(cols.get(i).trim(), cols.get(i + 2).trim());
			}
		}
		
		moveFile(item, path);
		
		out.putItem(item + ";" + prepareStringRepresentation(table));
	}
	
	public void parse(String fileName) throws Exception {
		Parser parser = new Parser(fileName);
		NodeFilter nf = new TagNameFilter("td");
		NodeList list = parser.parse(nf);

		cols = getTableContents(list);
	}
	
	private List<String> getTableContents(NodeList list) {
		List<String> result = null;
		
		for (int i = 0; i < list.size(); i++) {
			Node node = list.elementAt(i);
			if (node.getClass() == TableColumn.class) {
				TableColumn td = (TableColumn)node;
				result = addLink(result, td.getStringText());	
			}
		}
		
		return result;
	}
	
	private List<String> addLink(List<String> links, String link) {
		if (links == null) {
			links = new ArrayList<String>();
		}
		
		links.add(link);
		
		return links;
	}

	private String prepareStringRepresentation(Map<String, String> table) {
		Set<String> keysSet = table.keySet();
		String[] resultArray = new String[9];
		String result = "";
		
		for (String key : keysSet) {

			if (key.contains("IČO")) {
				resultArray[0] = table.get(key).replaceAll("\\<[^>]*>","").trim();
			} else if (key.contains("firma")) {
				resultArray[1] = table.get(key).replaceAll("\\<[^>]*>","").trim();
			} else if (key.contains("forma:")) {
				resultArray[2] = table.get(key).replaceAll("\\<[^>]*>","").trim();
			} else if (key.contains("Datum vz")) {
				resultArray[3] = table.get(key).replaceAll("\\<[^>]*>","").trim();
			} else if (key.contains("Datum z")) {
				resultArray[4] = table.get(key).replaceAll("\\<[^>]*>","").trim();
			} else if (key.contains("Velikostn")) {
				resultArray[5] = table.get(key).replaceAll("\\<[^>]*>","").trim();
			} else if (key.contains("dle CZ")) {
				resultArray[6] = table.get(key).replaceAll("\\<[^>]*>","").trim();
			} else if (key.contains("sektor")) {
				resultArray[7] = table.get(key).replaceAll("\\<[^>]*>","").trim();
			} else if (key.contains("Okres")) {
				resultArray[8] = table.get(key).replaceAll("\\<[^>]*>","").trim();
			}
		}

		for (String resPart : resultArray) {
			if (resPart != null) {
				result += resPart.replaceAll(";", "-") + ";";
			}
		}
		
		return result;
	}
	
	private void moveFile(String originalFile, String path) {
	    File file = new File(originalFile);
	    File dir = new File(path);
	    
	    if (!dir.exists()) {
	    	dir.mkdirs();
	    }
	    
	    // Move file to new directory
	    boolean success = file.renameTo(new File(dir, file.getName()));
	    if (!success) {
	        log.error("File " + originalFile + "was not moved!");
	    }
	}

}