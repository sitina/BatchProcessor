package net.sitina.bp.modules;

import net.sitina.bp.api.Hub;
import net.sitina.bp.api.Module;
import net.sitina.bp.api.ModuleConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class DateParserModule extends Module {

    private Set<Integer> positions = new HashSet<>();
    private char delimiter = ',';

    public DateParserModule(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
        super(in, out, config, instanceNumber);
        loadConfiguration();
    }

    @Override
    protected void process(String item) {
        for (int position : positions) {
            int start = nthOccurrence(item, delimiter, position - 1);
            int end = nthOccurrence(item, delimiter, position);

            if (start > -1) {
                String dateSubstring = item.substring(start + 1);
                if (end > -1) {
                    dateSubstring = dateSubstring.substring(0, end - start - 1);
                }
                String formattedDate = processDate(dateSubstring);

                if (end > -1) {
                    item = item.substring(0, start + 1) + formattedDate + item.substring(end);
                } else {
                    item = item.substring(0, start + 1) + formattedDate;
                }
            }
        }

        out.putItem(item);
    }

    protected String processDate(String dateSubstring) {

        dateSubstring = dateSubstring.substring(1, dateSubstring.length() - 1);

        int first = nthOccurrence(dateSubstring, '.', 0);
        int second = nthOccurrence(dateSubstring, '.', 1);

        if (first > -1 && second > -1) {
            try {
                int day = Integer.valueOf(dateSubstring.substring(0, first));
                int month = Integer.valueOf(dateSubstring.substring(first + 1, second));
                int year = Integer.valueOf(dateSubstring.substring(second + 1));

                return String.format("%d-%02d-%02d", year, month, day);
            } catch (Exception e) {
                log.info("Problem parsing date " + dateSubstring);
            }
        }
        return "'" + dateSubstring + "'";
    }

    @Override
    protected void loadConfiguration() {
        if (configuration.containsKey("positions")) {
            String positionsStr = configuration.getStringProperty("positions");
            StringTokenizer strTok = new StringTokenizer(positionsStr, ",");

            while (strTok.hasMoreTokens()) {
                String position = strTok.nextToken();
                try {
                    positions.add(Integer.valueOf(position));
                } catch (Exception e) {
                    log.error("Value '" + position + "' cannot be converted into the number.");
                }
            }
        }

        if (configuration.containsKey("delimiter")) {
            delimiter = configuration.getStringProperty("delimiter").charAt(0);
        }
    }

    protected int nthOccurrence(String str, char c, int n) {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1)
            pos = str.indexOf(c, pos+1);
        return pos;
    }

}
