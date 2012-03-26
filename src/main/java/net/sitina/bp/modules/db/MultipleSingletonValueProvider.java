package net.sitina.bp.modules.db;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sitina.bp.modules.cache.Memoizer;

import org.apache.log4j.Logger;

public class MultipleSingletonValueProvider {

    private final static Map<String, ValueProvider<String, Long>> instances = new ConcurrentHashMap<String, ValueProvider<String, Long>>();

    private final static Logger log = Logger.getLogger(MultipleSingletonValueProvider.class);

    public static ValueProvider<String, Long> getValueProvider(Connection connection, String tableName) {
        if (!instances.containsKey(tableName)) {
            synchronized (Lock.class) {
                if (!instances.containsKey(tableName)) {
                    instances.put(tableName, getCachingValueProvider(connection, tableName));
                }
            }
        }
        log.debug(instances);
        return instances.get(tableName);
    }

    private static ValueProvider<String, Long> getCachingValueProvider(Connection connection, String key) {
        DatabaseValueProvider dvp = new DatabaseValueProvider(connection, key);
        Memoizer<String, Long> m = new Memoizer<String, Long>(dvp);
        return m;
    }

    private class Lock {
    }

}