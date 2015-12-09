package net.sitina.bp.impl;

import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.sitina.bp.api.Hub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryHub implements Hub {

    // this size limit means count of items, not their actual size in bytes/whatever else
    private static final int SIZE_LIMIT = 500000;

    private static final Logger log = LoggerFactory.getLogger(InMemoryHub.class);

    private boolean isComplete = false;

    private Long counter = Long.valueOf(0);

    private AbstractQueue<String> items = new ConcurrentLinkedQueue<String>();

    @Override
    public synchronized void setComplete() {
        isComplete = true;
    }

    @Override
    public synchronized boolean isComplete() {
        return (isComplete && items.isEmpty());
    }

    @Override
    public synchronized String getItem() {
        return items.poll();
    }

    @Override
    public void putItem(String item) {
        if (item != null) {
            synchronized (counter) {
                if (counter++ > SIZE_LIMIT) {
                    while (items.size() > SIZE_LIMIT / 100) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            log.error("Execution interrupted", e);
                        }
                    }

                    counter = Long.valueOf(0);
                }
            }

            synchronized (items) {
                items.add(item);
            }
        }
    }

}