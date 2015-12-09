package net.sitina.bp.util;

public class BPStringTokenizer {

    private String text;

    private String delimiter;

    public BPStringTokenizer(String text, String delimiter) {
        this.text = text;
        this.delimiter = delimiter;
    }

    public boolean hasMoreTokens() {
        return text.length() > 0 || text.contains(delimiter);
    }

    public String nextToken() {
        String result;
        if (text.indexOf(delimiter) > -1) {
            result = text.substring(0, text.indexOf(delimiter));
            text = text.substring(text.indexOf(delimiter) + 1);
        } else {
            result = text;
            text = "";
        }
        return result;
    }

}
