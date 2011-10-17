package net.sitina.bp.impl;

import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import net.sitina.bp.api.Hub;

public class InMemoryHub implements Hub {

	private static final int SIZE_LIMIT = 500000;
	
	private static final Logger log = Logger.getLogger(InMemoryHub.class);
	
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
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							log.error(e);
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