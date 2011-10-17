package net.sitina.bp.api;

import java.util.Observable;

import org.apache.log4j.Logger;

public abstract class Module extends Observable implements Runnable {

	protected Hub in;
	
	protected Hub out;
	
	protected int instanceNumber;
	
	protected ModuleConfiguration configuration;
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	public Module(Hub in, Hub out, ModuleConfiguration config, int instanceNumber) {
		this.in = in;
		this.out = out;
		this.configuration = config;
		this.instanceNumber = instanceNumber;
	}
	
	@Override
	public void run() {
		String itemToProcess = null;
		while (!in.isComplete()) {
			
			if ((itemToProcess = in.getItem()) != null) {
				log.debug("Processing " + itemToProcess);
				try {
					process(itemToProcess);
				} catch (Exception e) {
					log.error("Exception in processing item '" + itemToProcess + "'", e);
				}
				Thread.yield(); // give chance also to other threads/modules
			} else {
				try {
					// log.debug("Nothing to process, waiting");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.error(e);
				}
			}
		}
		
		log.info("Processing complete");
		
		out.setComplete();
	}
	
	protected abstract void process(String item);
	
	protected abstract void loadConfiguration();
	
}
